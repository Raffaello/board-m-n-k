package game.types

import game.BitBoardPlayers
import game.boards.BoardPlayers

/**
  * TODO need numplayers...
  */
trait BoardMNTypeBitBoard extends BoardMNType with BoardPlayers {
  lazy private[this] val _board: BitBoardPlayers = Array.ofDim(numPlayers)

  override protected def board: BitBoardPlayers = _board

}
