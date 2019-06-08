package game.boards

import game.types.Position

trait BoardMovesConsumer {
  def consumeMoves()(f: Position => Unit): Unit
}
