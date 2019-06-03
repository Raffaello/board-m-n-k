package object game {
  // Todo generalize score to be numeric?
  type Score = Int

  // todo generalize as a numeric ?
  type Player = Byte

  type Board1d = Array[Player]
  type Board2d = Array[Array[Player]]
  type BitBoard = Int
  type BitBoardPlayers = Array[BitBoard]
}

