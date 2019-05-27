package game.types

import game.Board1d

import scala.collection.immutable.NumericRange

trait BoardMNType1dArray extends BoardMNType {
  val mn = m * n
  protected var _board: Board1d = Array.ofDim[Byte](mn)


  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)
  val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, m)

}
