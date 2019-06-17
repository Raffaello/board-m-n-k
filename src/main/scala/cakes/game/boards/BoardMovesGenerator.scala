package cakes.game.boards

import types.Position

trait BoardMovesGenerator {
  def generateMoves(): IndexedSeq[Position]
}
