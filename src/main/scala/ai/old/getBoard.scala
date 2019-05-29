package ai.old

import game.BoardMNK

// TODO remove/refactor
trait getBoard extends BoardMNK {
  override def board: Board = super.board
}
