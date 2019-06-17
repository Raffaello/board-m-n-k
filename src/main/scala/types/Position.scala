package types
/*
sealed trait Position {
  def row: Short
  def col: Short
}

case object NilPosition extends Position {
  override def row: Short = -1

  override def col: Short = -1
}

final case class Position2(row: Short, col: Short) extends Position {
  def min: Short = Math.min(row, col).toShort
}
 */

final case class Position(row: Short, col: Short) {
  def min: Short = Math.min(row, col).toShort
}

object Position {
  val nil: Position = Position(-1, -1)
}
