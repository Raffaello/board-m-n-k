package ai.old

import game.BoardMNK

// TODO remove/refactor
trait GetBoard extends BoardMNK {
  override def board: Board = super.board
}
