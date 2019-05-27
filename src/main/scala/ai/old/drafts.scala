package ai.old

import ai.Stats
import game.{BitBoardTicTacToe, BoardTicTacToe1dArray, Player, Position}

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
        if game.playMove(Position(i, j), 1)
      } {
        best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
        a = Math.max(a, best)
        game.undoMove(Position(i, j), 1.toByte)
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
        game.undoMove(Position(i, j), 2.toByte)
        if (alpha >= b) {
          return best
        }
      }

      best
    }
  }

  def alphaBetaBit(game: BitBoardTicTacToe, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    if (game.gameEnded(depth)) {
//      println(game.toStringArray)
      return game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    }

    Stats.totalCalls += 1
    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      val p: Player = 1.toByte
      for {
        i <- 0 until  3
        j <- 0 until  3
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
      val p: Player = 2.toByte
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
