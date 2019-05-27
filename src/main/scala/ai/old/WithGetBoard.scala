package ai.old

import game.{Board, Board2dArray, BoardMNK}

trait WithGetBoard extends BoardMNK with Board2dArray {
  def board: Board = _board
}
