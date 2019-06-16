package ai.simple

import game.types.Position
import game.{Board1d, Player}
import game.Implicit._

import scala.collection.mutable.ListBuffer
import scala.util.Random

package object mcts {

  // Implementation of a basic MCTS Tic-Tac-Toe game
  Random.setSeed(0)

  class TicTacToeState() {
    var board: Board1d = Array.ofDim[Player](9)
    var lastPlayer = 2

    def currentPlayer(): Player = 3 - lastPlayer

    def playMove(move: Position): Unit = {
      val i = move.row * 3 + move.col
      assert(board(i) == 0)
      val player = currentPlayer()
      board(i) = player
      lastPlayer = player
    }

    def allRemainingMoves(): IndexedSeq[Position] = {
      for {
        i <- 0 until 3
        j <- 0 until 3
        if board(i * 3 + j) == 0
      } yield Position(i, j)
    }

    // current player?
    def result(playerJustMove: Player): Double = {
      val winMoves: Seq[(Player, Player, Player)] = Seq(
        (0, 1, 2), (3, 4, 5), (6, 7, 8), // rows
        (0, 3, 6), (1, 4, 7), (2, 5, 8), // cols
        (0, 4, 8), (2, 4, 6) // diags
      )

      for ((x, y, z) <- winMoves) {
        if (board(x) == board(y) && board(y) == board(z)) {
          if (board(x) == playerJustMove) return 1.0
          else return 0.0
        }
      }

      assert(allRemainingMoves().isEmpty)
      0.5
    }

    def copy(): TicTacToeState = {
      val c = new TicTacToeState
      Array.copy(board, 0, c.board, 0, 9)
      c
    }

    override def toString: String = {
      var str = new StringBuffer()

      for (i <- board.indices) {
        if (i % 3 == 0) str.append("\n")

        str.append(
          board(i) match {
            case 1 => "X"
            case 2 => "O"
            case 0 => "_"
          }
        )
      }
      str.append("\n").toString
    }
  }

  class Node(val move: Option[Position], val parent: Option[Node], val state: TicTacToeState) {
    var wins: Double = 0.0
    var visits: Int = 0
    var untriedMoves = state.allRemainingMoves()
    var children: ListBuffer[Node] = new ListBuffer[Node]()
    var lastPlayer = state.lastPlayer

    def UCTSelectChild(): Node = {
      children.maxBy {
        case c if c.visits == 0 => Double.MaxValue
        case c => c.wins / c.visits + Math.sqrt(2.0 * Math.log(this.visits / c.visits))
      }
    }

    def addChild(m: Option[Position], s: TicTacToeState): Node = {
      val n = new Node(m, Some(this), s)
      untriedMoves = untriedMoves.filterNot(m.contains(_))
      children += n
      n
    }

    def update(score: Double): Unit = {
      visits += 1
      wins += score
    }
  }

  def mctsMove(rootState: TicTacToeState, itermax: Int): Option[Position] = {
    val rootNode = new Node(None, None, rootState)

    for (_ <- 0 until itermax) {
      var node = rootNode
      val state = rootState.copy()

      // select
      while (node.untriedMoves.isEmpty && node.children.nonEmpty) {
        node = node.UCTSelectChild()
        state.playMove(node.move.get)
      }

      // expand
      if (node.untriedMoves.nonEmpty) {
        val indexes = node.untriedMoves.indices
        val randomIndex = Random.nextInt(indexes.length)
        val m = node.untriedMoves(randomIndex)

        state.playMove(m)
        node.addChild(Some(m), state)
      }

      // simulation
      while (state.allRemainingMoves().nonEmpty) {
        val moves = state.allRemainingMoves()
        val indexes = moves.indices
        val randomIndex = Random.nextInt(indexes.length)
        state.playMove(moves(randomIndex))
      }

      // backpropagation
      while (node != null) {
        node.update(state.result(node.lastPlayer))
        node = node.parent.orNull
      }
    }

    rootNode.children.maxBy(c => c.visits).move
  }
}
