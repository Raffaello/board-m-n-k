package game.types

import game.Player

import scala.collection.immutable.NumericRange

private[game] trait BoardMNType {
  type Board

  val m: Short

  val n: Short

  lazy val mn: Int = m * n
  lazy val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  lazy val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  protected def board: Board

  @inline
  protected def boardPlayer(pos: Position): Player

  @inline
  protected def boardPlayer_=(pos: Position)(p: Player): Unit
}
