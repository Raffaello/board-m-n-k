package ai

import game.{BoardMNK, Score}

/**
  * Only for 2 player at the moment.
  */
/*private[ai]*/ trait AiBoard extends BoardMNK with AiStats {
  def solve: Score
}
