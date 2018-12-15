package object game {
  type Board = Array[Array[Byte]]
  type Position = (Short, Short)
  type Status[T] = (T, Position)
}
