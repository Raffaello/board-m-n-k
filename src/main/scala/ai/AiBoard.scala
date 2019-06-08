package ai

import game.Implicit.convertToPlayer
import game.types.Status
import game.{BoardMNKP, Player, Score}

trait AiBoard extends BoardMNKP with AiStats {
  final val aiPlayer: Player = config.getInt("player")

  def solve: Score

  def nextMove: Status[Score]
}
