package game.boards.concrete

import cats.implicits._
import game.boards.{BoardMoves, BoardT}
import game.types.{BoardMNType1dArray, Position}
import game.{Board1d, Player}

/**
  * TODO the Position of the board can be stored and reused, are always the same n*m classes... consideration.
  */
trait Board1dArray extends BoardMNType1dArray with BoardT with BoardMoves {

  override protected def board: Board1d = _board

  private[this] def boardOffset(pos: Position): Int = mLookups(pos.row) + pos.col

  protected def board(pos: Position): Byte = {
    // TODO look up i*m as a im
    _board(boardOffset(pos))
  }

  protected def board_=(pos: Position)(p: Player): Unit = _board(boardOffset(pos)) = p

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(boardOffset(Position(i, j))) === 0
    } yield Position(i, j)
    //    for {
    //      i <- mnIndices
    //      if _board(i) === 0
    //    } yield Position((i/m).toShort, (i%m).toShort)
  }
}
