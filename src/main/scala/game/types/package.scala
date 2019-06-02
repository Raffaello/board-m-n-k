package game

package types {

  final case class Position(row: Short, col: Short)

  final case class Status[T: Numeric](score: T, position: Position)

}
