import ai.types.{AlphaBetaStatus, AlphaBetaValues}
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import game.Implicit.convertToPlayer
import game._
import game.types.{Position, Status}

package object ai {

  // TODO refactor/remove
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  private[ai] val logger = Logger("ai")

  val config: Config = settings.Loader.config.getConfig("ai")

  def alphaBeta(
                 game: BoardMNK, depth: Int = 0,
                 ab: AlphaBetaValues[Double] = AlphaBetaValues.alphaBetaValueDouble,
                 maximizingPlayer: Boolean = true
               ): Double = {

    if (game.gameEnded(depth)) {
      game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    } else {
      Stats.totalCalls += 1
      if (maximizingPlayer) {
        var best = Double.MinValue
        var a = ab.alpha
        val player: Player = 1
        for {
          p <- game.generateMoves()
          if game.playMove(p, player)
        } {
          best = Math.max(best, alphaBeta(game, depth + 1, AlphaBetaValues(a, ab.beta), false))
          a = Math.max(a, best)
          game.undoMove(p, player)
          if (a >= ab.beta) {
            return best
          }
        }

        best
      } else {
        var best = Double.MaxValue
        var b = ab.beta
        val player: Player = 2
        for {
          p <- game.generateMoves()
          if game.playMove(p, player)
        } {
          best = Math.min(best, alphaBeta(game, depth + 1, AlphaBetaValues(ab.alpha, b), true))
          b = Math.min(b, best)
          game.undoMove(p, player)
          if (ab.alpha >= b) {
            return best
          }
        }

        best
      }
    }
  }

  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, ab: AlphaBetaValues[Double], maximizingPlayer: Boolean): AlphaBetaStatus[Double] = {
    var pBest = Position(-1, -1)

    if (game.gameEnded(depth)) {
      val score = game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
      AlphaBetaStatus(ab, Status(score, pBest))
    } else {
      var best: Double = 0.0
      var player: Player = 0
      var a = ab.alpha
      var b = ab.beta

      if (maximizingPlayer) {
        best = Double.MinValue
        player = 1
      }
      else {
        best = Double.MaxValue
        player = 2
      }
      for {
        p <- game.generateMoves()
        if game.playMove(p, player)
      } {
        val newBest = alphaBeta(game, depth + 1, AlphaBetaValues(a, b), !maximizingPlayer)

        if (maximizingPlayer) {
          if (newBest > best) {
            best = newBest
            pBest = p
          }
          a = Math.max(a, best)
        } else {
          if (newBest < best) {
            best = newBest
            pBest = p
          }

          b = Math.min(b, best)
        }

        game.undoMove(p, player)
        if (a >= ab.beta) {
          return AlphaBetaStatus(AlphaBetaValues(a, b), Status(best, pBest))
        }
      }

      AlphaBetaStatus(AlphaBetaValues(a, b), Status(best, pBest))
    }
  }

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, ab: AlphaBetaValues[Score] = AlphaBetaValues.alphaBetaValueScore, maximizingPlayer: Boolean = true): Transposition = {
    statuses.get() match {
      case Some(t) =>
        Stats.cacheHits += 1
        t
      case None if game.gameEnded(depth) =>
        val s = game.score()
        val score = (Math.round(s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
        val t = Transposition(
          score,
          depth,
          AlphaBetaValues(score, score),
          maximizingPlayer
        )

        statuses.add(t)
        t
      case _ =>
        // AlphaBeta:
        // Normal AlphaBeta cycle, managing Transposition
        // Basically duplicate code logic here compared
        // to AlphaBeta, differences:
        // the recursive function and its returning type
        // plus adding the state visisted to transposition table.
        // Note: This logic is "merged" in the traits implementation.
        Stats.totalCalls += 1
        if (maximizingPlayer) {
          var best = Int.MinValue
          var a = ab.alpha
          val player: Player = 1
          for {
            p <- game.generateMoves()
            if game.playMove(p, player)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, AlphaBetaValues(a, ab.beta), false)
            best = Math.max(best, t.score)
            a = Math.max(a, best)
            game.undoMove(p, player)
            if (a >= ab.beta) {
              return t
            }
          }

          val t = Transposition(best, depth, AlphaBetaValues(a, ab.beta), maximizingPlayer)
          statuses.add(t)
          t
        } else {
          var best = Int.MaxValue
          var b = ab.beta
          val player: Player = 2
          for {
            p <- game.generateMoves()
            if game.playMove(p, player)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, AlphaBetaValues(ab.alpha, b), true)
            best = Math.min(best, t.score)
            b = Math.max(b, best)
            game.undoMove(p, player)
            if (ab.alpha >= b) {
              return t
            }
          }

          val t = Transposition(best, depth, AlphaBetaValues(ab.alpha, b), maximizingPlayer)
          statuses.add(t)
          t
        }
    }
  }
}