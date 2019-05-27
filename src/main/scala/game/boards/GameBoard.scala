package game.boards

import game.Score
import game.types.Position

trait GameBoard {

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position, player: Byte): Boolean

  def score(): Score

  def gameEnded(): Boolean

  def gameEnded(depth: Int): Boolean

  def opponent(player: Byte): Byte

  def nextPlayer(): Byte

  def display(): String

  def consumeMoves()(f: Position => Unit): Unit
}
