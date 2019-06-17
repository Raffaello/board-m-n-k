package cakes

import cakes.ai.types.{AlphaBetaStatus, AlphaBetaValues}
import cakes.game.Implicit._
import cakes.game.types.Status
import cakes.game.{BoardMNK, Player, Score}
import com.typesafe.scalalogging.Logger
import types.Position

import scala.util.control.Breaks.{break, breakable}

package object ai {
  private[ai] val logger = Logger("cakes.ai")
  val aiPlayer: Player = settings.Loader.Ai.player

  // TODO refactor/remove
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  private[this] def scoreEval(game: BoardMNK, depth: Int): Double = {
    game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
  }

  def alphaBeta(
                 game: BoardMNK, depth: Int = 0,
                 ab: AlphaBetaValues[Double] = AlphaBetaValues.alphaBetaValueDouble,
                 maximizingPlayer: Boolean = true
               ): Double = {

    def maxBranch(): Double = {
      var best = Double.MinValue
      var a = ab.alpha
      val player: Player = 1

      breakable {
        for {
          p <- game.generateMoves()
          if game.playMove(p, player)
        } {
          best = Math.max(best, alphaBeta(game, depth + 1, AlphaBetaValues(a, ab.beta), false))
          a = Math.max(a, best)
          game.undoMove(p, player)
          if (a >= ab.beta) {
            break
          }
        }
      }

      best
    }

    def minBranch(): Double = {
      var best = Double.MaxValue
      var b = ab.beta
      val player: Player = 2

      breakable {
        for {
          p <- game.generateMoves()
          if game.playMove(p, player)
        } {
          best = Math.min(best, alphaBeta(game, depth + 1, AlphaBetaValues(ab.alpha, b), true))
          b = Math.min(b, best)
          game.undoMove(p, player)
          if (ab.alpha >= b) {
            break
          }
        }
      }

      best
    }

    if (game.gameEnded(depth)) scoreEval(game, depth)
    else {
      Stats.totalCalls += 1
      if (maximizingPlayer) maxBranch()
      else minBranch()
    }
  }

  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, ab: AlphaBetaValues[Double], maximizingPlayer: Boolean): AlphaBetaStatus[Double] = {
    var pBest = Position.nil

    if (game.gameEnded(depth)) {
      val score = scoreEval(game, depth)
      AlphaBetaStatus(ab, Status(score, pBest))
    } else {
      var best: Double = 0.0
      var player: Player = 0
      var a = ab.alpha
      var b = ab.beta

      best = if (maximizingPlayer) Double.MinValue else Double.MaxValue
      player = if (maximizingPlayer) aiPlayer else game.opponent(aiPlayer)

      game.consumeMoves { p =>
        game.playMove(p, player)
        val newBest = alphaBeta(game, depth + 1, AlphaBetaValues(a, b), !maximizingPlayer)

        def scoreCmp(a: Double, b: Double): Unit = {
          if (a > b) {
            best = newBest
            pBest = p
          }
        }

        if (maximizingPlayer) {
          scoreCmp(newBest, best)
          a = Math.max(a, best)
        } else {
          scoreCmp(best, newBest)
          b = Math.min(b, best)
        }

        game.undoMove(p, player)
        if (a >= b) {
          return AlphaBetaStatus(AlphaBetaValues(a, b), Status(best, pBest))
        }
      }

      AlphaBetaStatus(AlphaBetaValues(a, b), Status(best, pBest))
    }
  }

  def alphaBetaWithMem(
                        statuses: TranspositionTable,
                        game: BoardMNK,
                        depth: Int = 0,
                        ab: AlphaBetaValues[Score] = AlphaBetaValues.alphaBetaValueScore,
                        maximizingPlayer: Boolean = true
                      ): Transposition = {

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
        // plus adding the state visited to transposition table.
        // Note: This logic is "merged" in the traits implementation.
        Stats.totalCalls += 1
        var best = if (maximizingPlayer) Int.MinValue else Int.MaxValue
        var a = ab.alpha
        val player: Player = if (maximizingPlayer) aiPlayer else game.opponent(aiPlayer)
        var b = ab.beta

        game.consumeMoves { p =>
          game.playMove(p, player)
          val t = alphaBetaWithMem(statuses, game, depth + 1, AlphaBetaValues(a, ab.beta), !maximizingPlayer)
          if (maximizingPlayer) {
            best = Math.max(best, t.score)
            a = Math.max(a, best)
          } else {
            best = Math.min(best, t.score)
            b = Math.max(b, best)
          }

          game.undoMove(p, player)
          if (a >= b) {
            return t
          }
        }

        val t = Transposition(best, depth, AlphaBetaValues(a, ab.beta), maximizingPlayer)
        statuses.add(t)
        t
    }
  }
}
