package game.boards

import game.Player
import game.types.Position

trait BoardT {
  @inline
  protected def boardPlayer(pos: Position): Player

  @inline
  protected def boardPlayer_=(pos: Position)(p: Player): Unit
}
