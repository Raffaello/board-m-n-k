package game.boards.implementations

import cats.implicits._
import game.Player
import game.boards.BoardMovesGenerator
import game.boards.lookups.MLookup
import game.types.{BoardMNType1dArray, Position}

trait Board1dArray extends BoardMNType1dArray with BoardMovesGenerator with MLookup {

  @inline
  private[this] def boardOffset(pos: Position): Int = mLookups(pos.row) + pos.col

  protected def boardPlayer(pos: Position): Player = board(boardOffset(pos))

  protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(boardOffset(pos)) = p

  override def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(boardOffset(Position(i, j))) === 0
    } yield Position(i, j)
  }
}
