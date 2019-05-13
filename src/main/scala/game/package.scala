package object game {
  type Board = Array[Array[Byte]]
  type Position = (Short, Short)
  type Score = Int
  type Status = (Score, Position)

  class BoardTicTacToe extends BoardMNK(3, 3, 3)
}

