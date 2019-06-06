package ai.old

import ai.Stats
import game.Implicit.convertToPlayer
import game.types.Position
import game.{BitBoardTicTacToe, BoardMNK, Player}

object Drafts {
  // DUPLICTE, TODO remove this code
  def alphaBeta(
                 game: BoardMNK,
                 depth: Int = 0,
                 alpha: Double = Double.MinValue,
                 beta: Double = Double.MaxValue,
                 maximizingPlayer: Boolean = true
               ): Double = {
    if (game.gameEnded(depth)) {
      game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    } else {
      Stats.totalCalls += 1
      if (maximizingPlayer) {
        var best = Double.MinValue
        var a = alpha
        for {
          i <- game.mIndices
          j <- game.nIndices
          if game.playMove(Position(i, j), 1)
        } {
          best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
          a = Math.max(a, best)
          game.undoMove(Position(i, j), 1)
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
          if game.playMove(Position(i, j), 2)
        } {
          best = Math.min(best, alphaBeta(game, depth + 1, alpha, b, true))
          b = Math.min(b, best)
          game.undoMove(Position(i, j), 2)
          if (alpha >= b) {
            return best
          }
        }

        best
      }
    }
  }

  def alphaBetaBit(
                    game: BitBoardTicTacToe,
                    depth: Int = 0,
                    alpha: Double = Double.MinValue,
                    beta: Double = Double.MaxValue,
                    maximizingPlayer: Boolean = true
                  ): Double = {
    if (game.gameEnded(depth)) {
      return game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    }

    Stats.totalCalls += 1
    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      val p: Player = 1
      for {
        i <- 0 until 3
        j <- 0 until 3
        if game.playMove(Position(i.toShort, j.toShort), p)
      } {
        best = Math.max(best, alphaBetaBit(game, depth + 1, a, beta, false))
        a = Math.max(a, best)
        game.undoMove(Position(i.toShort, j.toShort), p)
        if (a >= beta) {
          return best
        }
      }

      best
    } else {
      var best = Double.MaxValue
      var b = beta
      val p: Player = 2
      for {
        i <- 0 until 3
        j <- 0 until 3
        if game.playMove(Position(i.toShort, j.toShort), p)
      } {
        best = Math.min(best, alphaBetaBit(game, depth + 1, alpha, b, true))
        b = Math.min(b, best)
        game.undoMove(Position(i.toShort, j.toShort), p)
        if (alpha >= b) {
          return best
        }
      }

      best
    }
  }
}
