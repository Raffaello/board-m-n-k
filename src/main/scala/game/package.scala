import game.types.Position

package object game {
  type Score = Int
  type Status = (Score, Position)
  type Player = Byte

  // TODO remove Board Type definition... those should be abstract "Board".
  type Board1d = Array[Player]
  type Board2d = Array[Array[Player]]
  type BitBoard = Int
  type BitBoardPlayers = Array[BitBoard]
}

