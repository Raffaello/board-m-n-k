package ai

import game.{BoardMNK, Score, Status}

/**
  * Only for 2 player at the moment.
  */
trait AiBoard extends BoardMNK with AiStats {
  // TODO AiPlayer is coded as player 1
  // TODO can be loaded by the config file too.
  final val aiPlayer: Byte = 1

  def solve: Score

  def nextMove: Status
}
