import game.types.Position

package object game {
  type Board = Board2d
  type Score = Int
  type Status = (Score, Position)
  type Player = Byte
  type Board2d = Array[Array[Byte]]
  type Board1d = Array[Byte]
//  type BitBoard = mutable.BitSet
  type BitBoard = Int
}

