package ai

import game.Score

trait MiniMaxRaw extends MiniMax {

  override def solve(maximizing: Boolean): Score = {
    if (gameEnded()) {
      scoreEval
    } else {
      Stats.totalCalls += 1
      val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min
      var value: Int = if (maximizing) Int.MinValue else Int.MaxValue
      val pl = if (maximizing) aiPlayer else nextPlayer()
      consumeMoves() { p =>

        playMove(p, pl)
        value = cmp(value, solve(!maximizing))
        undoMove(p, pl)
      }

      value
    }
  }
}
