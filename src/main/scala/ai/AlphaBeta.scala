package ai

import game.{Position, Score, Status}

trait AlphaBeta extends AiBoard {
  protected var _alphaBetaNextMove: AB[Score] = (Int.MinValue, Int.MaxValue)

  def alphaBetaNextMove: AB[Score] = _alphaBetaNextMove

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
        val s = eval(abStatus)
        best = s._2._1
        a = s._1._1
        b = s._1._2
        undoMove(p, player)

        if (a >= b) {
          return best
        }
      }

      best
    }
  }

  def solve(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Score = {
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

  def solve: Score = solve()

  override def nextMove: Status = {
    val (ab, status) = nextMove(nextPlayer() == aiPlayer, _alphaBetaNextMove)
    _alphaBetaNextMove = ab
    status
  }

  protected def nextMove(maximizing: Boolean = true, ab: AB[Score]): ABStatus[Score] = {
    val (alpha, beta) = ab
    nextMove(maximizing, depth, alpha, beta)
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
