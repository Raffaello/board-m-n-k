package ai

import game.{BoardMNKP, Score, Status}

trait AiBoard extends BoardMNKP with AiStats {
  final val aiPlayer: Byte = config.getInt("player").toByte

  def solve: Score

  def nextMove: Status
}
