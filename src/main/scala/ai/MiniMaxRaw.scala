package ai

import game.Score

trait MiniMaxRaw extends MiniMax {

  override def solve(maximizing: Boolean = true, depth: Int = 0): Score = {
    if (gameEnded(depth)) {
      score()
    } else {
      Stats.totalCalls += 1
      val cmp: (Int, Int) => Int = if(maximizing) Math.max else Math.min
      var value: Int = if(maximizing) Int.MinValue else Int.MaxValue
      val pl = player(maximizing)
      consumeMoves() { p =>

        playMove(p, pl)
        value = cmp(value, solve(!maximizing, depth + 1))
        undoMove(p, pl)
      }

      value
    }
  }
}
