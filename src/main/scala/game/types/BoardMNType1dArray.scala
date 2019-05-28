package game.types

import game.Board1d

import scala.collection.immutable.NumericRange

trait BoardMNType1dArray extends BoardMNType {
  protected var _board: Board1d = Array.ofDim[Byte](mn)

  val mnIndices: Range = 0 to mn
  val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, m)
}
