package cakes.game.boards

import cakes.game.types.Position

trait LastMoveTracker {
  protected var _lastMove: Position = Position(0, 0)

  def lastMove: Position = _lastMove
}
