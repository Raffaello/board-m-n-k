package object game {
  type Board = Array[Array[Byte]]
  // todo refactor position with a case class x,y ... ????
  type Position = (Short, Short)
  type Score = Int
  type Status = (Score, Position)
}

