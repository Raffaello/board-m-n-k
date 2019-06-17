package cakes.game.boards

import cakes.game.{Player, Score}
import types.Position

trait GameBoard {

  def playMove(position: Position, player: Player): Boolean

  def undoMove(position: Position, player: Player): Boolean

  /**
    * if player won 1
    * if player lost -1
    * if it is a draw 0
    */
  def score(player: Player): Score

  def gameEnded(): Boolean

  def gameEnded(depth: Int): Boolean

  def opponent(player: Player): Player

  def nextPlayer(): Player

  protected def checkWin(): Boolean

}
