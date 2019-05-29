package game.boards.concrete

import cats.implicits._
import game.Player
import game.boards.{BoardMovesGenerator, BoardT}
import game.types.{BoardMNType1dArray, Position}

/**
  * TODO the Position of the board can be stored and reused, are always the same n*m classes... consideration.
  */
trait Board1dArray extends BoardMNType1dArray with BoardT with BoardMovesGenerator {

  private[this] def boardOffset(pos: Position): Int = mLookups(pos.row) + pos.col

  protected def boardPlayer(pos: Position): Player = {
    // TODO look up i*m as a im
    board(boardOffset(pos))
  }

  protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(boardOffset(pos)) = p

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(boardOffset(Position(i, j))) === 0
    } yield Position(i, j)
    //    for {
    //      i <- mnIndices
    //      if _board(i) === 0
    //    } yield Position((i/m).toShort, (i%m).toShort)
  }
}
