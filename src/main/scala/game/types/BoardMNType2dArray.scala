package game.types

import game.Board2d

import scala.collection.immutable.NumericRange

trait BoardMNType2dArray extends BoardMNType {
  protected var _board: Board2d = Array.ofDim[Byte](m, n)
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)
}
