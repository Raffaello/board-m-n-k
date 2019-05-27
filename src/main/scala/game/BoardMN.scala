package game

/**
  * this is generic BoardMN, board2dArray should be the concrete implementation of this one.
  * now is the other way arround. REDESIGN.
  */
abstract class BoardMN(val boardMNSize: BoardMNSize) extends GameBoard {
  @inline
  val m: Short = boardMNSize.m
  @inline
  val n: Short = boardMNSize.n

  val mnMin: Short = Math.min(m, n).toShort

  protected var freePositions: Int = m * n
  protected var _lastMove: Position = Position(0, 0)

  def lastMove: Position = _lastMove

  protected def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  def stdoutPrintln(): Unit = println(display())
}
