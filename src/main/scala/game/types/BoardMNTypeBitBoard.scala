package game.types

import game.BitBoardPlayers
import game.boards.BoardPlayers

/**
  * TODO need numplayers...
  */
trait BoardMNTypeBitBoard extends BoardMNType with BoardPlayers {
  private[this] var _board: BitBoardPlayers = Array.ofDim(numPlayers)

  override protected def board: BitBoardPlayers = _board

}
