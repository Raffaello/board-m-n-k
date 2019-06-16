package ai.cakes.old

import game.BoardMNK
import game.boards.implementations.Board2dArray

class BoardMNKwithGetBoard(m: Short, n: Short, k: Short) extends BoardMNK(m, n, k)
  with Board2dArray with GetBoard
