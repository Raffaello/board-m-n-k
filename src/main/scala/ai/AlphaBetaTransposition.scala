package ai

import game.Score
import util.control.Breaks._

trait AlphaBetaTransposition extends AlphaBeta with TranspositionTable {

  // TODO could be refactored to reuse the super.mainBlock
  override protected def mainBlock(maximizing: Boolean, alpha: Int, beta: Int)(eval: ABStatus[Score] => ABStatus[Score]): Score = {
    get() match {
      case Some(t) =>
        Stats.cacheHits += 1
        t.score
      case None if gameEnded() =>
        val s2 = scoreEval
        val ab = (s2, s2)
        add(Transposition(s2, depth, ab, maximizing))
        s2
      case _ =>
        Stats.totalCalls += 1
        var best = if (maximizing) Int.MinValue else Int.MaxValue
        var a = alpha
        var b = beta
        val player: Byte = if (maximizing) 1 else 2

        breakable {
          consumeMoves() { p =>
            playMove(p, player)
            val abStatus: ABStatus[Score] = ((a, b), (best, p))
            val ((a0, b0), (score, _)) = eval(abStatus)
            best = score
            a = a0
            b = b0
            undoMove(p, player)

            if (a >= b) {
              break
            }
          }

          val t = Transposition(best, depth, (a, b), maximizing)
          add(t)
        }

        best
    }
  }
}
