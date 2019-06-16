package cakes.game.boards.implementations

import cats.implicits._
import cakes.game.Implicit.convertToPlayer
import cakes.game.Player
import cakes.game.boards.BoardMovesGenerator
import cakes.game.types.{BoardMNType2dArray, Position}

trait Board2dArray extends BoardMNType2dArray with BoardMovesGenerator {
  protected def boardPlayer(pos: Position): Player = board(pos.row)(pos.col)

  protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(pos.row)(pos.col) = p

  override def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if boardPlayer(Position(i, j)) === 0
    } yield Position(i, j)
  }
}
