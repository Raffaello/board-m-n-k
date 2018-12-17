package object game {
  type Board = Array[Array[Byte]]
  type Position = (Short, Short)
  type Status = (Int, Position)

  case class Transposition(score: Double, depth: Int, alpha: Double, beta: Double, isMaximizing: Boolean)
}
