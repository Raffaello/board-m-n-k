package game

package types {

  final case class Position(row: Short, col: Short)

  final case class Status[T: Numeric](score: T, position: Position)


  sealed trait BoardMNTypeEnum

  case object BOARD_2D_ARRAY extends BoardMNTypeEnum

  case object BOARD_1D_ARRAY extends BoardMNTypeEnum

  case object BOARD_BIT_BOARD extends BoardMNTypeEnum

}
