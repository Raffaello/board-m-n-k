package cakes.game

package types {

  import _root_.types.Position

  final case class Status[T: Numeric](score: T, position: Position)

  sealed trait BoardMNTypeEnum

  case object BOARD_2D_ARRAY extends BoardMNTypeEnum

  case object BOARD_1D_ARRAY extends BoardMNTypeEnum

  /**
    * This one is only for 2 players at the moment
    */
  case object BOARD_BIT_BOARD extends BoardMNTypeEnum

}
