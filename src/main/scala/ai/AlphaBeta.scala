package ai

import cats.implicits._
import game.{Position, Score, Status}

trait AlphaBeta extends AiBoard with AlphaBetaNextMove {

  protected def mainBlock(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue)(eval: ABStatus[Score] => ABStatus[Score]): Score = {
    if (gameEnded(depth)) {
      val s = score()
      Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
    } else {
      Stats.totalCalls += 1
      var best = if (maximizing) Int.MinValue else Int.MaxValue
      var a = alpha
      var b = beta
      val player: Byte = if (maximizing) 1 else 2

      consumeMoves() { p =>
        playMove(p, player)
        val abStatus: ABStatus[Score] = ((a, b), (best, p))
        val((a0, b0), (score, _)) = eval(abStatus)
        best = score
        a = a0
        b = b0
        undoMove(p, player)

        if (a >= b) {
          return best
        }
      }

      best
    }
  }

  def solve(maximizing: Boolean, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Score = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min
    var a1 = alpha
    var b1 = beta
    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, depth + 1, a, b)
      val best = cmp(v, value)
      if (maximizing) a1 = Math.max(a, best)
      else b1 = Math.min(b, best)
      ((a1, b1), (best, p))
    }
  }

  def solve: Score = solve(aiPlayer === nextPlayer())

  override def nextMove: Status = {
    val (a, b) = alphaBetaNextMove
    val (ab, status) = nextMove(nextPlayer() === aiPlayer, depth, a, b)
    _alphaBetaNextMove = ab
    status
  }

  protected def nextMove(maximizing: Boolean, depth: Int, alpha: Int, beta: Int): ABStatus[Score] = {
    var pBest: Position = (-1, -1)
    var best = if (maximizing) Int.MinValue else Int.MaxValue
    var a1 = alpha
    var b1 = beta
    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, depth + 1, a, b)

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
