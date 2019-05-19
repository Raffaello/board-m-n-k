package ai.old

import ai.Stats
import game.BoardTicTacToe1dArray

object drafts {
  def alphaBeta(game: BoardTicTacToe1dArray, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    if (game.gameEnded(depth)) {
      return game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    }

    Stats.totalCalls += 1
    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      for {
        i <- game.mIndices
        j <- game.nIndices
        if game._board(i * game.m + j) == 0
      }  {
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
}
