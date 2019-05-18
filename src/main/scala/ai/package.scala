import game._

//noinspection NameBooleanParameters
package object ai {
  // TODO refactor
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }
  type AB[T] = (T, T) // Alpha, Beta values
  type ABStatus[T] = (AB[T], Status) // Alpha, Beta values plus Status: Score, Position

  /**
    * TODO: replace return type Double with Score (Int)
    * @return
    */
  def alphaBeta(game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    if (game.gameEnded(depth)) {
      //      return game.score()
      //      return game.score() * (1.0/(depth + 1))
      return game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))

    }

    Stats.totalCalls += 1
    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      for {
        i <- game.mIndices
        j <- game.nIndices
        if game.playMove((i, j), 1)
      } {
        best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
        a = Math.max(a, best)
        game.undoMove((i, j), 1)
        if (a >= beta) {
          return best
        }
      }

      best
    } else {
      var best = Double.MaxValue
      var b = beta
      for {
        i <- game.mIndices
        j <- game.nIndices
        if game.playMove((i, j), 2)
      } {
        best = Math.min(best, alphaBeta(game, depth + 1, alpha, b, true))
        b = Math.min(b, best)
        game.undoMove((i, j), 2)
        if (alpha >= b) {
          return best
        }
      }

      best
    }
  }

  /**
    * @return (score, x pos, y pos, alpha, beta)
    */
  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): old.ABMove = {
    var ibest: Short = -1
    var jbest: Short = -1

    if (game.gameEnded(depth)) {
      (game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0))), (ibest, jbest), (alpha, beta))
    } else {
      var best: Double = 0.0
      var player: Byte = 0
      var a = alpha
      var b = beta

      if (maximizingPlayer) {
        best = Double.MinValue
        player = 1
      }
      else {
        best = Double.MaxValue
        player = 2
      }
      for {
        i <- game.mIndices
        j <- game.nIndices
        if game.playMove((i, j), player)
      } {
        val newBest = alphaBeta(game, depth + 1, a, b, !maximizingPlayer)

        if (maximizingPlayer) {
          if (newBest > best) {
            best = newBest
            ibest = i
            jbest = j
          }
          a = Math.max(a, best)
        } else {
          if (newBest < best) {
            best = newBest
            ibest = i
            jbest = j
          }

          b = Math.min(b, best)
        }

        game.undoMove((i, j), player)
        if (a >= beta) {
          return (best, (ibest, jbest), (a, b))
        }
      }

      (best, (ibest, jbest), (a, b))
    }
  }

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue, maximizingPlayer: Boolean = true): Transposition = {
    val transposition = statuses.get()
    if (transposition.isDefined) {
      Stats.cacheHits += 1
      transposition.get
    } else if (game.gameEnded(depth)) {
      val s = game.score()
      val score = (Math.round(s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
      val t = Transposition(
        score,
        depth,
        (score, score),
        maximizingPlayer
      )

      statuses.add(t)
      t
    } else {

      Stats.totalCalls += 1
      if (maximizingPlayer) {
        var best = Int.MinValue
        var a = alpha
        for {
          i <- game.mIndices
          j <- game.nIndices
          if game.playMove((i, j), 1)
        } {
          val t = alphaBetaWithMem(statuses, game, depth + 1, a, beta, false)
          best = Math.max(best, t.score)
          a = Math.max(a, best)
          game.undoMove((i, j), 1)
          if (a >= beta) {
            return t
          }
        }

        val t = Transposition(best, depth, (a, beta), maximizingPlayer)
        statuses.add(t)
        t
      } else {
        var best = Int.MaxValue
        var b = beta
        for {
          i <- game.mIndices
          j <- game.nIndices
          if game.playMove((i, j), 2)
        } {
          val t = alphaBetaWithMem(statuses, game, depth + 1, alpha, b, true)
          best = Math.min(best, t.score)
          b = Math.max(b, best)
          game.undoMove((i, j), 2)
          if (alpha >= b) {
            return t
          }
        }

        val t = Transposition(best, depth, (alpha, b), maximizingPlayer)
        statuses.add(t)
        t
      }
    }
  }
}
