package ai

import game.Score

trait AlphaBetaTransposition extends AlphaBeta with TranspositionTable {

  override def mainBlock(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue)(eval: ABStatus[Score] => ABStatus[Score]): Score = {
    val transposition = get()

    if (transposition.isDefined) {
      Stats.cacheHits += 1
      return transposition.get.score
    }

    // TODO refactor to allow to call super.mainBlock
    //super.mainBlock(maximizing, depth, alpha, beta)(eval)
    if (gameEnded(depth)) {
      val s = score()
      val s2 = Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
      val t = Transposition(
        s2,
        depth,
        (s2, s2),
        maximizing
      )

      add(t)
      t.score
    } else {
      Stats.totalCalls += 1
      var best = if (maximizing) Int.MinValue else Int.MaxValue
      var a = alpha
      var b = beta
      val player: Byte = if (maximizing) 1 else 2

      consumeMoves() { p =>
        playMove(p, player)
        val abStatus: ABStatus[Score] = ((a, b), (best, p))
        val ((a0, b0), (score, _)) = eval(abStatus)
        best = score
        a = a0
        b = b0
        undoMove(p, player)

        if (a >= b) {
          return score
        }
      }

      val t = Transposition(best, depth, (a, b), maximizing)
      add(t)
      t.score
    }
  }
}
