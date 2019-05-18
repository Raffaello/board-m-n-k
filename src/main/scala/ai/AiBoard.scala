package ai

import game.{BoardMNK, Score}

/**
  * Only for 2 player at the moment.
  */
trait AiBoard extends BoardMNK with AiStats {
  def solve: Score
}
