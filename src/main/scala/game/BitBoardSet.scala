package game

import scala.collection.immutable.NumericRange

abstract class BitBoardSet(m: Short, n: Short) extends BoardT with GameBoard {
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)
}
