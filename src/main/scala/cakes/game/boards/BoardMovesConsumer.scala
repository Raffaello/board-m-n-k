package cakes.game.boards

import cakes.game.types.Position

trait BoardMovesConsumer {
  // TODO should return something.... ?
  // Higher types? or ?? let's figure it out
  def consumeMoves(f: Position => Unit): Unit
}
