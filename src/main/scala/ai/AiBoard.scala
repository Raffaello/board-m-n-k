package ai

import game.{BoardMNK, Position, Score}

/**
  * Only for 2 player at the moment.
  */
trait AiBoard extends BoardMNK with AiStats {
  def solve: Score

  // TODO: keep internal the state for the next move...
  def nextMove: Position = ???
}
