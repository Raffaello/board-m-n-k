package ai.old

import game.BoardMNK
import game.types.BoardMNSize

/**
  * @deprecated
  */
class BoardMNKwithGetBoard(boardMNSize: BoardMNSize, k: Short) extends BoardMNK(boardMNSize, k) with getBoard
