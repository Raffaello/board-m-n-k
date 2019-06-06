package ai

import ai.types.{AlphaBetaStatus, AlphaBetaValues}
import cats.implicits._
import game.Score
import game.types.{Position, Status}

import scala.util.control.Breaks._

// TODO define the trait with a [T : Numeric] later on...?
trait AlphaBeta extends AiBoard with AlphaBetaNextMove with AiBoardScoreEval {

  protected def mainBlock(maximizing: Boolean, alphaBetaValues: AlphaBetaValues[Score])
                         (eval: AlphaBetaStatus[Score] => AlphaBetaStatus[Score]): Score = {

    if (gameEnded()) scoreEval
    else {
      Stats.totalCalls += 1
      var best = if (maximizing) Int.MinValue else Int.MaxValue
      var ab = alphaBetaValues
      val player: Byte = if (maximizing) 1 else 2

      breakable {
        // TODO consumeMoves should return here a value to be assigned instead
        consumeMoves() { p =>
          playMove(p, player)
          val abStatus: AlphaBetaStatus[Score] = AlphaBetaStatus(ab, Status(best, p))
          val newAbStatus = eval(abStatus)
          best = newAbStatus.status.score
          ab = newAbStatus.alphaBetaValues
          undoMove(p, player)

          if (ab.isAlphaGteBeta) {
            break
          }
        }
      }

      best
    }
  }

  def solve(maximizing: Boolean, alphaBetaValues: AlphaBetaValues[Score]): Score = {
    lazy val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min
    var a = alphaBetaValues.alpha
    var b = alphaBetaValues.beta
    val score = mainBlock(maximizing, alphaBetaValues) { abStatus: AlphaBetaStatus[Score] =>
      val value = solve(!maximizing, abStatus.alphaBetaValues)
      val best = cmp(abStatus.status.score, value)
      if (maximizing) a = Math.max(abStatus.alphaBetaValues.alpha, best)
      else b = Math.min(abStatus.alphaBetaValues.beta, best)
      // TODO use LENSES / Monocle for deep copy
      AlphaBetaStatus(AlphaBetaValues(a, b), Status(best, abStatus.status.position))
    }

    score
  }

  def solve: Score = solve(aiPlayer === nextPlayer(), AlphaBetaValues.alphaBetaValueScore)

  override def nextMove: Status[Score] = {
    val abStatus: AlphaBetaStatus[Score] = nextMove(nextPlayer() === aiPlayer, alphaBetaNextMove)
    _alphaBetaNextMove = abStatus.alphaBetaValues
    abStatus.status
  }

  protected def nextMove(maximizing: Boolean, alphaBetaValues: AlphaBetaValues[Score]): AlphaBetaStatus[Score] = {
    var pBest: Position = Position(-1, -1)
    var best = if (maximizing) Int.MinValue else Int.MaxValue
    var (a1, b1) = (alphaBetaValues.alpha, alphaBetaValues.beta)
    // TODO mainBlock should return to avoid var
    mainBlock(maximizing, alphaBetaValues) { abStatus: AlphaBetaStatus[Score] =>
      val value = solve(!maximizing, abStatus.alphaBetaValues)
      if (maximizing) {
        if (value > abStatus.status.score) {
          best = value
          pBest = abStatus.status.position
          a1 = Math.max(abStatus.alphaBetaValues.alpha, best)
        }
      } else {
        if (value < abStatus.status.score) {
          best = value
          b1 = Math.min(abStatus.alphaBetaValues.beta, best)
          pBest = abStatus.status.position
        }
      }

      AlphaBetaStatus[Score](AlphaBetaValues(a1, b1), Status(best, abStatus.status.position))
    }

    AlphaBetaStatus[Score](AlphaBetaValues(a1, b1), Status(best, pBest))
  }
}
