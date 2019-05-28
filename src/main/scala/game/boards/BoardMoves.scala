package game.boards

import game.types.Position

trait BoardMoves {
  def generateMoves(): IndexedSeq[Position]

  def consumeMoves()(f: Position => Unit): Unit
}
