package game.types

import game.Board1d

import scala.collection.immutable.NumericRange

trait BoardMNType1dArray extends BoardMNType {
  private[this] var _board: Board1d = Array.ofDim[Byte](mn)

  lazy val mnIndices: Range = 0 to mn
  lazy val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, m)

  override protected def board: Board1d = _board
}
