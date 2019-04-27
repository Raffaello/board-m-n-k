package ai

trait MiniMaxRaw extends MiniMax {

  override def solve(maximizing: Boolean = true, depth: Int = 0): Int = {
    if (gameEnded(depth)) {
      score()
    } else {
      Stats.totalCalls += 1
      if (maximizing) {
        var value = Int.MinValue

        consumeMoves() { p =>
          playMove(p, 1)
          value = Math.max(value, solve(!maximizing, depth + 1))
          undoMove(p, 1)
        }

        value
      } else {
        var value = Int.MaxValue

        consumeMoves() { p =>
          playMove(p, 2)
          value = Math.min(value, solve(!maximizing, depth + 1))
          undoMove(p, 2)
        }

        value
      }
    }
  }
}
