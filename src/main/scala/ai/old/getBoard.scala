package ai.old

import game.{Board2d, BoardMNK}

// TODO remove/refactor
trait getBoard extends BoardMNK {
  override def board: Board2d = super.board
}
