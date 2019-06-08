package game.boards.implementations

import cats.implicits._
import game.Implicit.convertToPlayer
import game.boards.BoardMovesGenerator
import game.boards.lookups.BitBoardLookup
import game.types.{BoardMNTypeBitBoard, Position}
import game.{BitBoard, Player}

trait BoardBitBoard extends BoardMNTypeBitBoard with BoardMovesGenerator with BitBoardLookup {

  @inline
  private[this] def boardValue(position: Position): BitBoard = bitsLookup.getOrElse(position, bitValue(position))

  override protected def boardPlayer(pos: Position): Player = {
    val v = boardValue(pos)
    for {
      p <- 0 until numPlayers
      if (board(p) & v) === v
    } return p + 1

    0
  }

  override protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(p - 1) ^= boardValue(pos)

  override def generateMoves(): IndexedSeq[Position] = {
    val b = board.foldLeft(0)((acc, x) => acc | x)
    for {
      i <- mIndices
      j <- nIndices
      if (boardValue(Position(i, j)) & b) === 0
    } yield Position(i, j)
  }
}
