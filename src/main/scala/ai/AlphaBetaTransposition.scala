package ai

import ai.types.{AlphaBetaStatus, AlphaBetaValues}
import game.Score
import game.types.Status

import scala.util.control.Breaks._

trait AlphaBetaTransposition extends AlphaBeta with TranspositionTable {

  // TODO could be refactored to reuse the super.mainBlock
  override protected def mainBlock(maximizing: Boolean, alphaBetaValues: AlphaBetaValues[Score])
                                  (eval: AlphaBetaStatus[Score] => AlphaBetaStatus[Score]): Score = {
    get() match {
      case Some(t) =>
        Stats.cacheHits += 1
        t.score
      case None if gameEnded() =>
        val s2 = scoreEval
        add(Transposition(s2, depth, AlphaBetaValues(s2, s2), maximizing))
        s2
      case _ =>
        Stats.totalCalls += 1
        var best = if (maximizing) Int.MinValue else Int.MaxValue
        var ab = alphaBetaValues
        lazy val player: Byte = if (maximizing) 1 else 2

        breakable {
          consumeMoves() { p =>
            playMove(p, player)
            val abStatus = eval(AlphaBetaStatus[Score](ab, Status(best, p)))
            best = abStatus.status.score
            ab = abStatus.alphaBetaValues
            undoMove(p, player)

            if (ab.isAlphaGteBeta) {
              break
            }
          }

          val t = Transposition(best, depth, ab, maximizing)
          add(t)
        }

        best
    }
  }
}
