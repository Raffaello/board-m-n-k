package object game {
  type Board = Array[Array[Byte]]
//  type BoardClone = IndexedSeq[IndexedSeq[Byte]]
  type Position = (Short, Short)

  type Score = Int
  type Status = (Score, Position)
}
