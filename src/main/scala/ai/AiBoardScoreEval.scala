package ai

import game.{BoardDepthAware, GameBoard, Score}

trait AiBoardScoreEval extends GameBoard with BoardDepthAware {
  protected def scoreEval: Score = {
    val s = score()
    Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
  }
}
