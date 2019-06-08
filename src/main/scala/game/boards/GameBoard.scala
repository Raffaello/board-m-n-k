package game.boards

import game.types.Position
import game.{Player, Score}

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
