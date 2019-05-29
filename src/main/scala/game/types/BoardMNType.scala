package game.types

import game.Player
import game.boards.BoardMovesGenerator

import scala.collection.immutable.NumericRange

// TODO convert to ad hoc polymorphism? Type Classes?
trait BoardMNType extends BoardMovesGenerator {
  type Board

  val m: Short

  val n: Short

  lazy val mn: Int = m * n
  lazy val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  lazy val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  protected def board: Board

  @inline
  protected def boardPlayer(pos: Position): Player

  @inline
  protected def boardPlayer_=(pos: Position)(p: Player): Unit
}
