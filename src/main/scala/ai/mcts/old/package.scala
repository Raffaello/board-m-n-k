package ai.mcts

import ai.AiBoard
import ai.old.withGetBoard
import game.BoardTicTacToe

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

package object old {

  class BoardTicTacToeMcts extends BoardTicTacToe with withGetBoard

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
  class BoardState(m: Short, n: Short) extends BoardTicTacToeMcts with AiBoard {
    var player: Byte = _
    var visitCount: Int = 0
    var stateScore: Double = 0.0

    /**
      * todo case class .copy ???
      * @param state
      */
    def clone(state: BoardState) = {
      for {
        i <- mIndices
        j <- nIndices
        if state._board(i)(j) > 0
      } {
        this._board(i)(j) = state._board(i)(j)
      }

      this._depth = state._depth
      this.player = state.player
      this.visitCount = state.visitCount
      this.stateScore = state.stateScore
    }

    /**
      * TODO: REFACTOR not part of board state
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

    /**
      * TODO refactor, not part of board state
      */
    def randomMove(): Boolean = {
      val moves = generateMoves()
      if (moves.nonEmpty) {
        moves.lift(scala.util.Random.nextInt(moves.size)) match {
          case Some(p) => playMove(p, player)
          case None => false
        }
      } else false
    }

    def opponent(): Byte = super.opponent(player)
  }
}
