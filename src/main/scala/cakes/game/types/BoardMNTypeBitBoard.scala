package cakes.game.types

import cakes.game.{BitBoard, BitBoardPlayers}
import cakes.game.boards.BoardPlayers

private[game] trait BoardMNTypeBitBoard extends BoardMNType with BoardPlayers {
  type Board = BitBoardPlayers

  lazy private[this] val _board: Board = Array.ofDim(numPlayers)

  override protected def board: Board = _board

  // TODO for generating moves faster... not implemented yet
  protected val _boardFreePos: BitBoard = -1 // all Free

}
