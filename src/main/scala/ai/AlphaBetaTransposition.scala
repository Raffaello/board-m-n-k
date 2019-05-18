package ai

import game.{Position, Score}

trait AlphaBetaTransposition extends AiBoard with TranspositionTable {

  protected def mainBlock(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue)(eval: ABStatus[Score] => Transposition): Transposition = {
    val transposition = get()

    if (transposition.isDefined) {
      Stats.cacheHits += 1
      return transposition.get
    }

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
      t
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
        best = s.score
        val (a0, b0) = s.ab
        a = a0
        b = b0
        undoMove(p, player)

        if (a >= b) {
          return s
        }
      }

      val t = Transposition(best, depth, (a, b), maximizing)
      add(t)
      t
    }
  }

  def solve(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Transposition = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min
    var a1 = alpha
    var b1 = beta
    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, _)) =>
      val value = solve(!maximizing, depth + 1, a, b)
      val best = cmp(v, value.score)
      if (maximizing) a1 = Math.max(a, best)
      else b1 = Math.min(b, best)
      Transposition(best, depth, (a1, b1), maximizing)
    }
  }

  def solve: Score = solve().score

  def nextMove(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Transposition = {
    var pBest: Position = (-1, -1)
    var best = if (maximizing) Int.MinValue else Int.MaxValue
    var a1 = alpha
    var b1 = beta
    mainBlock(maximizing, depth, alpha, beta) { case ((a, b), (v, p)) =>
      val value = solve(!maximizing, depth + 1, a, b)
      if (maximizing) {
        if (value.score > v) {
          best = value.score
          pBest = p
          a1 = Math.max(a, best)
        }
      } else {
        if (value.score < v) {
          best = value.score
          b1 = Math.min(b, best)
          pBest = p
        }
      }

      Transposition(best, depth, (a1, b1), maximizing)
    }
  }
}
