package ai

import game.{BoardMNK, Score}

trait AiScoreEval extends BoardMNK {
  protected def scoreEval: Score = {
    val s = score()
    Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
  }
}
