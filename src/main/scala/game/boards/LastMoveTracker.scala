package game.boards

import game.types.Position

trait LastMoveTracker {
  protected var _lastMove: Position = Position(0, 0)

  def lastMove: Position = _lastMove
}
