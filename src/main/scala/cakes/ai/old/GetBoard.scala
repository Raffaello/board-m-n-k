package cakes.ai.old

import cakes.game.BoardMNK

// TODO remove/refactor
trait GetBoard extends BoardMNK {
  abstract override def board: Board = super.board
}
