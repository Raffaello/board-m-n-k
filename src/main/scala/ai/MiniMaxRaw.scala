package ai

trait MiniMaxRaw extends MiniMax {
  override def solve(maximizing: Boolean = true, depth: Int = 0): Int = {
    if (gameEnded(depth)) {
      score()
    } else {
      Stats.totalCalls += 1
      val cmp: (Int, Int) => Int = if(maximizing) Math.max else Math.min
      var value: Int = if(maximizing) Int.MinValue else Int.MaxValue

      consumeMoves() { p =>
        playMove(p, 1)
        value = cmp(value, solve(!maximizing, depth + 1))
        undoMove(p, 1)
      }

      value
    }
  }
}
