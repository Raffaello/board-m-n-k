package types

final case class Position(row: Short, col: Short) {
  def min: Short = Math.min(row, col).toShort
}

object Position {
  val nil: Position = Position(-1, -1)
}