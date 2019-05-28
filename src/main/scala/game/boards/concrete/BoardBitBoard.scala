package game.boards.concrete

import cats.implicits._
import game.boards.{BoardMovesGenerator, BoardT}
import game.types.{BoardMNTypeBitBoard, Position}
import game.{BitBoard, Player}

trait BoardBitBoard extends BoardMNTypeBitBoard with BoardT with BoardMovesGenerator {

  private[this] def boardValue(position: Position): BitBoard = {
    assert(position.row >= 0 && position.row < 3)
    assert(position.col >= 0 && position.col < 3)
    (1 << (position.row * m + position.col)) << mn
  }

//  private[this] def boardValueCheck(position: Position): BitBoard = {
  //    assert(position.row >= 0 && position.row < 3)
  //    assert(position.col >= 0 && position.col < 3)
  //    val v = boardValue(position)
  //
  //    board.find(bp => (bp & v) === 1) match {
  //      case Some(board) => board
  //      case None => 0
  //    }
  //  }

  override protected def board(pos: Position): Player = {
    val v = boardValue(pos)
    _board.zipWithIndex.collectFirst{ case (b, i) if (b & v) === v => i} match {
      case None => 0.toByte
      case Some(i) => i.toByte
    }
  }

  override protected def board_=(pos: Position)(p: Player): Unit = _board(p) ^= boardValue(pos)

  override def generateMoves(): IndexedSeq[Position] = {
      for {
        i <- mIndices
        j <- nIndices
        if _board(boardValue(Position(i, j))) === 0
      } yield Position(i, j)
  }
}
