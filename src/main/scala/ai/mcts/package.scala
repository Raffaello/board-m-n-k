package ai

import game.BoardTicTacToe

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

package object mcts {

  object UCT {
    final val c: Double = Math.sqrt(2.0)

    def UCT(w: Double, n: Int, N: Int): Double = {
      val res = n match {
        case 0 => Double.MaxValue
        case _ => w / n.toDouble + c * Math.sqrt(Math.log(N) / n.toDouble)
      }
      assert(N != 0)
      assert(!res.isNaN)
      assert(!res.isInfinity)
      res
    }

    def findBestNode(node: Node): Node = {
      // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
      node.children.maxBy(c => UCT(c.state.stateScore, c.state.visitCount, node.state.visitCount))
    }
  }

  class Node {
    var state: BoardState = _
    // TODO refactor with Option
    var parent: Node = null
    // TODO use a fixed array/list if possible.
    var children: ArrayBuffer[Node] = new ArrayBuffer[Node]()

    def randomChild(): Node = {
      val size = children.length
      size match {
        case 0 => this
        case _ => children(scala.util.Random.nextInt(children.length))
      }
    }
  }

  /**
    * TODO define a simpler board state
    * split in traits/refactor the other boards too
    */
  class BoardState(m: Short, n: Short) extends BoardTicTacToe with AiBoard {
    var player: Byte = _
    var visitCount: Int = 0
    var stateScore: Double = 0.0

    def clone(state: BoardState) = {
      for {
        i <- mIndices
        j <- nIndices
        if state.board(i)(j) > 0
      } {
        this.board(i)(j) = state.board(i)(j)
      }

      this.depth = state.depth
      this.player = state.player
      this.visitCount = state.visitCount
      this.stateScore = state.stateScore
    }

    /**
      * TODO: REFACTOR
      */
    def allPossibleStates(): List[BoardState] = {
      val states: ListBuffer[BoardState] = new ListBuffer[BoardState]()
      generateMoves().foreach { position =>
        //        val (x, y) = position
        val newState = new BoardState(m, n)
        newState.clone(this)
        //        newState.player = opponent()
        newState.playMove(position, newState.player)
        //        newState.board(x)(y) = newState.player
        states += newState
      }

      states.toList
    }

    def randomMove(): Boolean = {
      val moves = generateMoves()
      if (moves.nonEmpty) {
        moves.lift(scala.util.Random.nextInt(moves.size)) match {
          case Some(p) => playMove(p, player)
          case None => false
        }
      } else false
    }

    // TODO: assuming player 1 or 2
    def opponent(): Byte = (3 - player).toByte
  }
}
