package cakes.ai

import cakes.game.boards.{BoardDepthAware, GameBoard}
import cakes.game.{Player, Score}

trait AiBoardScoreEval extends GameBoard with BoardDepthAware {
  protected def scoreEval(player: Player): Score = {
    val s = score(player)
    Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
  }
}