package game.boards

import game.types.{BoardMNSize, BoardMNType, Position}

abstract class BoardMN(boardMNSize: BoardMNSize) extends GameBoard with BoardT with BoardMNType
  with BoardMovesGenerator with BoardMovesConsumer {

  @inline override val m: Short = boardMNSize.m
  @inline override val n: Short = boardMNSize.n

  lazy val mnMin: Short = Math.min(m, n).toShort
  protected var freePositions: Int = mn

  def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  // TODO remove and move into a trait
  def stdoutPrintln(): Unit = println(display())
}
