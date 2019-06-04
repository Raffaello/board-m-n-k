package ai.old

import game.BoardMNK
import game.boards.implementations.Board2dArray

// TODO generalize with other  array boards too.
class BoardMNKwithGetBoard(m: Short, n: Short, k: Short) extends BoardMNK(m, n, k)
  with Board2dArray with GetBoard
