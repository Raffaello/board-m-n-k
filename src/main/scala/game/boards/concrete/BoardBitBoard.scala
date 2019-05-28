package game.boards.concrete

import game.Player
import game.boards.{BoardMovesGenerator, BoardT}
import game.types.{BoardMNTypeBitBoard, Position}

trait BoardBitBoard extends BoardMNTypeBitBoard with BoardT with BoardMovesGenerator {
  override protected def board(pos: Position): Byte = ???

  override protected def board_=(pos: Position)(p: Player): Unit = ???

  override def generateMoves(): IndexedSeq[Position] = ???
}
