package game

import scala.collection.immutable.NumericRange

abstract class BoardMN(val m: Short, val n: Short) {
  //  require(m > 2 && n > 2)

  val mnMin: Short = Math.min(m, n).toShort
  val mIndices = NumericRange[Short](0, m, 1)
  val nIndices = NumericRange[Short](0, n, 1)

  protected val board: Board = Array.ofDim[Byte](m, n)

  protected var freePositions: Int = m * n
  protected var lastMove: Position = (0, 0)

  /**
    * @deprecated
    */
  def getBoard(): Board = board

  def move(position: Position) = board(position._1)(position._2)

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position): Unit

  def score(): Int

  def gameEnded(depth: Int): Boolean
}
