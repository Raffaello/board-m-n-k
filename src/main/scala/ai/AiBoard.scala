package ai

import game.types.Status
import game.{BoardMNKP, Score}

trait AiBoard extends BoardMNKP with AiStats {
  final val aiPlayer: Byte = config.getInt("player").toByte

  def solve: Score

  def nextMove: Status[Score]
}
