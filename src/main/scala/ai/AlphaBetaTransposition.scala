package ai

import ai.types.{AlphaBetaStatus, AlphaBetaValues}
import cats.implicits._
import game.{Player, Score}
import game.types.Status

import scala.util.control.Breaks._

trait AlphaBetaTransposition extends AlphaBeta with TranspositionTable {

  // TODO could be refactored to reuse the super.mainBlock
  // TODO has to return transposition
  override protected def mainBlock(player: Player, alphaBetaValues: AlphaBetaValues[Score])
                                  (eval: AlphaBetaStatus[Score] => AlphaBetaStatus[Score]): Score = {
    lazy val maximizing: Boolean = aiPlayer === player
    get() match {
      case Some(t) =>
        Stats.cacheHits += 1
        t.score
      case None if gameEnded() =>
        val s2 = scoreEval(aiPlayer)
        add(Transposition(s2, depth, AlphaBetaValues(s2, s2), maximizing))
        s2
      case _ =>
        Stats.totalCalls += 1
        var best = initValue(player)
        var ab = alphaBetaValues

        breakable {
          consumeMoves { p =>
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

