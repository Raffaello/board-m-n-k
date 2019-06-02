import game.types.Position

package object game {
  // Todo generalize score to be numeric.
  type Score = Int
  // status old is/will be used only for ai.old, move there?
  type StatusOld = (Score, Position)
  // todo generalize as a numeric ?
  type Player = Byte

  // TODO remove Board Type definition... those should be abstract "Board".
  type Board1d = Array[Player]
  type Board2d = Array[Array[Player]]
  type BitBoard = Int
  type BitBoardPlayers = Array[BitBoard]
}

