package ai

import game.types.Status
import game.{BoardMNKP, Player, Score}

trait AiBoard extends BoardMNKP with AiStats {
  protected def signum(value: Boolean): Byte = if (value) +1 else -1

  final lazy val aiPlayer: Player = ai.aiPlayer

  def solve: Score

  def nextMove: Status[Score]
}
