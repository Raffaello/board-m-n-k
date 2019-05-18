package ai.old

import game.{Board, BoardMNK}

trait WithGetBoard extends BoardMNK {
  def board: Board = _board
}
