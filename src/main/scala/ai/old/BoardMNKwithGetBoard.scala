package ai.old

import game.{BoardMNK, BoardMNSize}

/**
  * @deprecated
  */
class BoardMNKwithGetBoard(boardMNSize: BoardMNSize, k: Short) extends BoardMNK(boardMNSize, k) with WithGetBoard
