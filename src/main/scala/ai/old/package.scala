package ai

import cats.implicits._
import game.Implicit.convertToPlayer
import game.types.Position
import game.{Board2d, BoardMNK, Score}

/**
  * @deprecated
  * Used for bench.benchmarks.
  */
package object old {
  type AB[T] = (T, T) // Alpha, Beta values // === AlphaBetaValues
  type ABMove = (Double, Position, AB[Double]) // score, position, Alpha, Beta // === AlphaBetaStatus
  type StatusOld = (Score, Position) // === Status

  def minimax(game: BoardMNK, isMaximizingPlayer: Boolean): Score = {
    def minMaxLoop(maximizing: Boolean): Int = {
      var best: Int = 0
      var cmp: (Int, Int) => Int = null
      var player: Byte = 0

      Stats.totalCalls += 1
      if (maximizing) {
        best = Int.MinValue
        cmp = Math.max
        player = 1
      }
      else {
        best = Int.MaxValue
        cmp = Math.min
        player = 2
      }

      for {
        p <- game.generateMoves()
        if game.playMove(p, player)
      } {
        best = cmp(best, minimax(game, !maximizing))
        game.undoMove(p, player)
      }

      best
    }

    if (game.gameEnded()) {
      game.score()
    } else {
      minMaxLoop(isMaximizingPlayer)
    }
  }

  /**
    * do not use.
    * Doesn't work as expected.
    */
  def negamax(game: BoardMNK, color: Byte): Score = {
    assert(color === 1 || color === -1)
    if (game.gameEnded()) {
      color * game.score()
    } else {

      Stats.totalCalls += 1
      var value = Int.MinValue
      var player = color
      if (player == -1) player = 2
      for {
        p <- game.generateMoves()
        if game.playMove(p, player)
      } {
        value = Math.max(value, -negamax(game, (-color).toByte))
        game.undoMove(p, player)
      }

      value
    }
  }

  def negamaxNextMove(game: BoardMNK, color: Byte): StatusOld = {
    assert(color === 1 || color === -1)
    if (game.gameEnded()) {
      (color * game.score(), game.lastMove)
    } else {

      var value = Int.MinValue
      var pBest = Position.nil
      var player = color
      if (player === -1) player = 2
      for {
        p <- game.generateMoves()
        if game.playMove(p, player)
      } {
        val newValue = -negamax(game, (-color).toByte)
        if (value < newValue) {
          value = newValue
          pBest = p
        }
        game.undoMove(p, player)
      }

      (value, pBest)
    }
  }

  def alphaBetaWithMem(
                        statuses: ai.old.TranspositionTable,
                        game: GetBoard,
                        depth: Int = 0,
                        alpha: Double = Double.MinValue,
                        beta: Double = Double.MaxValue,
                        maximizingPlayer: Boolean = true
                      ): ai.old.Transposition = {
    val transposition = statuses.get(game.board.asInstanceOf[Board2d])

    transposition match {
      case Some(t) =>
        Stats.cacheHits += 1
        t
      case None if game.gameEnded(depth) =>
        val score = game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
        val t = Transposition(
          score,
          depth,
          score,
          score,
          maximizingPlayer
        )

        statuses.add(game.board.asInstanceOf[Board2d], t)
        t
      case _ =>
        Stats.totalCalls += 1
        if (maximizingPlayer) {
          var best = Double.MinValue
          var a = alpha
          for {
            p <- game.generateMoves()
            if game.playMove(p, 1)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, a, beta, maximizingPlayer = false)
            best = Math.max(best, t.score)
            a = Math.max(a, best)
            game.undoMove(p, 1)
            if (a >= beta) {
              return t
            }
          }

          val t = Transposition(best, depth, a, beta, maximizingPlayer)
          statuses.add(game.board.asInstanceOf[Board2d], t)
          t
        } else {
          var best = Double.MaxValue
          var b = beta
          for {
            p <- game.generateMoves()
            if game.playMove(p, 2)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, alpha, b, maximizingPlayer = true)
            best = Math.min(best, t.score)
            b = Math.max(b, best)
            game.undoMove(p, 2)
            if (alpha >= b) {
              return t
            }
          }

          val t = Transposition(best, depth, alpha, b, maximizingPlayer)
          statuses.add(game.board.asInstanceOf[Board2d], t)
          t
        }
    }
  }
}

