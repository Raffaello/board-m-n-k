package game

package types {

  final case class Position(row: Short, col: Short) {
    def min: Short = Math.min(row, col).toShort
  }

  object Position {
    val nil: Position = Position(-1, -1)
  }

  final case class Status[T: Numeric](score: T, position: Position)

  sealed trait BoardMNTypeEnum

  case object BOARD_2D_ARRAY extends BoardMNTypeEnum

  case object BOARD_1D_ARRAY extends BoardMNTypeEnum

  case object BOARD_BIT_BOARD extends BoardMNTypeEnum

}
