package cakes.game.boards

import cakes.game.types.Position

trait BoardMovesGenerator {
  def generateMoves(): IndexedSeq[Position]
}
