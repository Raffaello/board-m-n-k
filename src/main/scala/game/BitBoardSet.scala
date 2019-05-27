package game

import game.boards.GameBoard

import scala.collection.immutable.NumericRange

abstract class BitBoardSet(m: Short, n: Short) extends GameBoard {
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)
}
