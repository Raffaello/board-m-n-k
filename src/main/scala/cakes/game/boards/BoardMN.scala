package cakes.game.boards

import cakes.game.types.BoardMNType
import types.Position

abstract class BoardMN(val m: Short, val n: Short) extends GameBoard
  with BoardMNType with BoardMovesGenerator with BoardMovesConsumer {
  lazy val mnMin: Short = Math.min(m, n).toShort
  protected var freePositions: Int = mn

  def consumeMoves(f: Position => Unit): Unit = generateMoves().foreach(f)
}
