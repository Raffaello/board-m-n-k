package game.types

import game.BitBoardPlayers
import game.boards.BoardPlayers

import scala.collection.immutable.NumericRange

trait BoardMNTypeBitBoard extends BoardMNType with BoardPlayers {
  type Board = BitBoardPlayers
  lazy private[this] val _board: Board = Array.ofDim(numPlayers)
  lazy val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, m)

  override protected def board: Board = _board

}
