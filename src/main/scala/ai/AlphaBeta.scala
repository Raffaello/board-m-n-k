package ai

import game.Position

trait AlphaBeta extends AiBoard {

//  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  def mainBlock(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue)(eval: ABStatus => Int): Int = {
    if (gameEnded(depth)) {
      val s = score()
      Math.round((s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
    } else {
      var best = if(maximizing) Int.MinValue else Int.MaxValue
      var a = alpha
      var b = beta
      val player: Byte = if (maximizing) 1 else 2

      consumeMoves() { p =>
        playMove(p, player)
        best = eval((a, b), (best,p))
        a = Math.max(a, best)
        b = Math.min(b, best)
        undoMove(p)

        if (a >= beta) {
          return best
        }
      }

      best
    }
  }

  def solve(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Int = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max _ else Math.min _

    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, depth + 1)
      cmp(v, value)
    }
  }

  def nextMove(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): ABStatus = {
    var pBest: Position = (-1, -1)
    var best = if (maximizing) Int.MinValue else Int.MaxValue

    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, depth + 1)

      if (maximizing) {
        if (value > v) {
          best = value
          pBest = p
        }
      } else {
        if (value < v) {
          best = value
        }
      }

      best
    }

    ((alpha, beta), (best, pBest))
  }
}
