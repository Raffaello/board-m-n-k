/*
package game.types

import game.boards.implementations.Board2dArray
import game.{Board2d, Player}

// TODO implement type class for type Board[T]
// where T can be board 2d,1d,bit at the moment...

trait BoardT[T] extends BoardMNType

object BoardT {
  def apply[T](implicit board: BoardT[T]): BoardT[T] = board

  implicit class BoardTOps[T: BoardT](t: T) {
      protected def board: T = t

    @inline
    protected def boardPlayer(pos: Position): Player = 0
  }

  // cannot be a val ... need 2 paramter in the constructor... :/
  implicit val board2dArray: BoardT[Board2d] = new BoardT[Board2d] with Board2dArray {
    override val m: Short = _
    override val n: Short = _
  }
}

 */