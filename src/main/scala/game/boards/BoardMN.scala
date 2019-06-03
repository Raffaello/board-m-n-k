package game.boards

import game.types.{BoardMNType, Position}

abstract class BoardMN(val m: Short, val n: Short)
  extends GameBoard with BoardMNType with BoardMovesGenerator with BoardMovesConsumer with BoardDisplay {

  lazy val mnMin: Short = Math.min(m, n).toShort
  protected var freePositions: Int = mn

  def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)
}
