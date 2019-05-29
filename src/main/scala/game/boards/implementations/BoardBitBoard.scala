package game.boards.implementations

import cats.implicits._
import game.types.{BoardMNTypeBitBoard, Position}
import game.{BitBoard, Player}

trait BoardBitBoard extends BoardMNTypeBitBoard {

  @inline
  private[this] def boardValue(position: Position): BitBoard = 1 << (mLookups(position.row) + position.col)

  override protected def boardPlayer(pos: Position): Player = {
    val v = boardValue(pos)
    for {
      p <- 0 until numPlayers
      if (board(p) & v) === v
    } return (p + 1).toByte

    0.toByte
//    board.zipWithIndex.collectFirst { case (b, i) if (b & v) === v => i + 1 }.getOrElse(0).toByte
  }

  override protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(p) ^= boardValue(pos)

  override def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(boardValue(Position(i, j))) === 0
    } yield Position(i, j)
  }
}
