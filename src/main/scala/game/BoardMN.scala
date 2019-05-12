package game

import scala.collection.immutable.NumericRange

abstract class BoardMN(val m: Short, val n: Short) {
  //  require(m > 2 && n > 2)

  val mnMin: Short = Math.min(m, n).toShort
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  protected var _board: Board = Array.ofDim[Byte](m, n)
  //  def board(): IndexedSeq[IndexedSeq[Byte]] = _board.map(_.toIndexedSeq).toIndexedSeq

  protected var freePositions: Int = m * n
  protected var _lastMove: Position = (0, 0)

  def lastMove(): Position = _lastMove

  final protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i)(j) == 0
    } yield (i, j)
  }

  protected def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position, player: Byte): Boolean

  def score(): Int

  def gameEnded(): Boolean

  def display(): String

  def stdoutPrintln(): Unit = println(display())
}
