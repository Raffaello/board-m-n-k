package game.boards.concrete

import cats.implicits._
import game.boards.{BoardMoves, BoardT}
import game.types.{BoardMNType2dArray, Position}
import game.{Board2d, Player}

// TODO: decouple the 2 used traits if it is possible. Type Classes?
trait Board2dArray extends BoardMNType2dArray with BoardT with BoardMoves {
  @inline
  protected def board(pos: Position): Byte = _board(pos.row)(pos.col)

  @inline
  protected def board_=(pos: Position)(p: Player): Unit = _board(pos.row)(pos.col) = p

  override protected def board: Board2d = _board

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(Position(i, j)) === 0
    } yield Position(i, j)
  }

  // TODO should not be here this is for a Board 2D Array with 2 players,
  //  this is trait is just a 2d array board implementation

}
