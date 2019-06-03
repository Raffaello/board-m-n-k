package game.boards

import game.{Player, Score}
import game.types.Position

trait GameBoard {

  def playMove(position: Position, player: Player): Boolean

  def undoMove(position: Position, player: Player): Boolean

  def score(): Score

  def gameEnded(): Boolean

  def gameEnded(depth: Int): Boolean

  def opponent(player: Player): Player

  def nextPlayer(): Player

  protected def checkWin(): Boolean

}
