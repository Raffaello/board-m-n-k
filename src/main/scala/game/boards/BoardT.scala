package game.boards

import game.Player
import game.types.Position

// TODO use a type class?
//trait BoardT[T <: BoardMNType] {
trait BoardT {
  protected def boardPlayer(pos: Position): Player

  protected def boardPlayer_=(pos: Position)(p: Player): Unit
}
