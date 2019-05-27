package ai

import cats.implicits._
import game.{Position, Score, Status}

import scala.util.control.Breaks._

trait AlphaBeta extends AiBoard with AlphaBetaNextMove with AiBoardScoreEval {

  protected def mainBlock(maximizing: Boolean, alpha: Int, beta: Int)(eval: ABStatus[Score] => ABStatus[Score]): Score = {
    if (gameEnded()) scoreEval
    else {
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
            break //((a, b), best)
          }
        }
      }

      best
    }
  }

  def solve(maximizing: Boolean, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Score = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min
    var a1 = alpha
    var b1 = beta
    val score = mainBlock(maximizing, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, a, b)
      val best = cmp(v, value)
      if (maximizing) a1 = Math.max(a, best)
      else b1 = Math.min(b, best)
      ((a1, b1), (best, p))
    }

    score
  }

  def solve: Score = solve(aiPlayer === nextPlayer())

  override def nextMove: Status = {
    val (a, b) = alphaBetaNextMove
    val (ab, status) = nextMove(nextPlayer() === aiPlayer, a, b)
    _alphaBetaNextMove = ab
    status
  }

  protected def nextMove(maximizing: Boolean, alpha: Int, beta: Int): ABStatus[Score] = {
    var pBest: Position = (-1, -1)
    var best = if (maximizing) Int.MinValue else Int.MaxValue
    var a1 = alpha
    var b1 = beta
    mainBlock(maximizing, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, a, b)

      if (maximizing) {
        if (value > v) {
          best = value
          pBest = p
          a1 = Math.max(a, best)
        }
      } else {
        if (value < v) {
          best = value
          b1 = Math.min(b, best)
          pBest = p
        }
      }

      ((a1, b1), (best, p))
    }

    ((a1, b1), (best, pBest))
  }
}
