package ai

import game.{BoardMN, DepthAware, Score}

trait AiBoardScoreEval extends BoardMN with DepthAware {
  protected def scoreEval: Score = {
    val s = score()
    Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
  }
}
