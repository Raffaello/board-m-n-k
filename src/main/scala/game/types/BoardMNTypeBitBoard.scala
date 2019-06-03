package game.types

import game.BitBoardPlayers
import game.boards.BoardPlayers

trait BoardMNTypeBitBoard extends BoardMNType with BoardPlayers {
  type Board = BitBoardPlayers

  lazy private[this] val _board: Board = Array.ofDim(numPlayers)

  override protected def board: Board = _board

}
