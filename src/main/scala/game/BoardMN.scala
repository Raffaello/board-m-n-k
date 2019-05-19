package game

abstract class BoardMN(val m: Short, val n: Short) extends Board2dArray(m, n) with GameBoard {
  require(m > 2 && n > 2)

  val mnMin: Short = Math.min(m, n).toShort

  protected var freePositions: Int = m * n
  protected var _lastMove: Position = (0, 0)

  def lastMove: Position = _lastMove

  protected def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  def stdoutPrintln(): Unit = println(display())
}
