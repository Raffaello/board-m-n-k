package game.boards

import game.types.Position

trait BoardMovesGenerator {
  def generateMoves(): IndexedSeq[Position]
}
