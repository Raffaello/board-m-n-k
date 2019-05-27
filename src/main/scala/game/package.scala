package object game {
  type Board = Board2d
  // todo refactor position with a case class x,y ... ????
  type Position = (Short, Short)
  type Score = Int
  type Status = (Score, Position)
  type Player = Byte
  type Board2d = Array[Array[Byte]]
  type Board1d = Array[Byte]
//  type BitBoard = mutable.BitSet
  type BitBoard = Int
}

