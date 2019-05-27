package game

abstract class BoardMN(val boardMNSize: BoardMNSize) extends GameBoard with BoardT {
  @inline val m: Short = boardMNSize.m
  @inline val n: Short = boardMNSize.n

  val mnMin: Short = Math.min(m, n).toShort

  def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  def stdoutPrintln(): Unit = println(display())
}
