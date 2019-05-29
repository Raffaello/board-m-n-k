package game.types

import game.Board1d

import scala.collection.immutable.NumericRange

trait BoardMNType1dArray extends BoardMNType {
  type Board = Board1d

  lazy private[this] val _board: Board = Array.ofDim[Byte](mn)

  lazy val mnIndices: Range = 0 to mn
  lazy val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, m)

  override protected def board: Board = _board
}
