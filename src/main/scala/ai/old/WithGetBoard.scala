package ai.old

import game.{Board2d, Board2dArray, BoardMNK, BoardMNType}

trait WithGetBoard extends BoardMNK with BoardMNType with Board2dArray {
  def board: Board2d = _board
}
