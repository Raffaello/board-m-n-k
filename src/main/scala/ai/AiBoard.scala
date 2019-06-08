package ai

import game.types.Status
import game.{BoardMNKP, Player, Score}

trait AiBoard extends BoardMNKP with AiStats {
  final lazy val aiPlayer: Player = ai.aiPlayer

  def solve: Score

  def nextMove: Status[Score]
}
