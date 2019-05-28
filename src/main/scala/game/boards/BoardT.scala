package game.boards

import game.Player
import game.types.Position

// TODO use a type class?
//trait BoardT[T <: BoardMNType] {
trait BoardT {
  protected def board(pos: Position): Player

  protected def board_=(pos: Position)(p: Player): Unit
}
