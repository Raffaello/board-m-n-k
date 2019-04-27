package game

import scala.collection.immutable.NumericRange

abstract class BoardMN(m: Short, n: Short) extends withBoard2D(m , n) {
  //  require(m > 2 && n > 2)

  val mnMin: Short = Math.min(m, n).toShort
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  protected var freePositions: Int = m * n
  protected var lastMove: Position = (0, 0)

  /**
    * @TODO: generalize types
    * wrappers
    */
  def board(position: PositionInt): Byte = board((position._1.toShort, position._2.toShort))
  def board(x: Int, y: Short): Byte = board((x.toShort, y.toShort))
  def board(x: Short, y: Int): Byte = board((x.toShort, y.toShort))

  def move(position: Position): Byte = board(position)

//  def playMove(position: Position, player: Byte): Boolean

//  def undoMove(position: Position, player: Byte): Unit

  def score(): Int

  def gameEnded(depth: Int): Boolean
}
