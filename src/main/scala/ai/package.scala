import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import game._
import game.types.Position

//noinspection NameBooleanParameters
package object ai {

  // TODO refactor/remove
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  // TODO review the type definitions.... they are not ok.
  //  final case class AlphaBeta[T](alpha: T, Beta: T)
  // todo refactor with a case class alpha, beta
  type AB[T] = (T, T) // Alpha, Beta values
  type ABStatus[T] = (AB[T], Status) // Alpha, Beta values plus Status: Score, Position
  type ABScore = (AB[Score], Score)

  private[ai] val logger = Logger("ai")

  val config: Config = settings.Loader.config.getConfig("ai")

  def alphaBeta(game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    if (game.gameEnded(depth)) {
      game.score() + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
    } else {
      Stats.totalCalls += 1
      if (maximizingPlayer) {
        var best = Double.MinValue
        var a = alpha
        for {
          p <- game.generateMoves()
          if game.playMove(p, 1)
        } {
          best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
          a = Math.max(a, best)
          game.undoMove(p, 1)
          if (a >= beta) {
            return best
          }
        }

        best
      } else {
        var best = Double.MaxValue
        var b = beta
        for {
          p <- game.generateMoves()
          if game.playMove(p, 2)
        } {
          best = Math.min(best, alphaBeta(game, depth + 1, alpha, b, true))
          b = Math.min(b, best)
          game.undoMove(p, 2)
          if (alpha >= b) {
            return best
          }
        }

        best
      }
    }
  }

  /**
    * TODO replace return type with a case class.
    *
    * @return (score, x pos, y pos, alpha, beta)
    */
  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): old.ABMove = {
    var ibest: Short = -1
    var jbest: Short = -1

    if (game.gameEnded(depth)) {
      (game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0))), Position(ibest, jbest), (alpha, beta))
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
        p <- game.generateMoves()
        if game.playMove(p, player)
      } {
        val newBest = alphaBeta(game, depth + 1, a, b, !maximizingPlayer)

        if (maximizingPlayer) {
          if (newBest > best) {
            best = newBest
            // TODO fix those 2 vars
            ibest = p.row
            jbest = p.col
          }
          a = Math.max(a, best)
        } else {
          if (newBest < best) {
            best = newBest
            // TODO fix those 2 vars
            ibest = p.row
            jbest = p.col
          }

          b = Math.min(b, best)
        }

        game.undoMove(p, player)
        if (a >= beta) {
          return (best, Position(ibest, jbest), (a, b))
        }
      }

      (best, Position(ibest, jbest), (a, b))
    }
  }

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue, maximizingPlayer: Boolean = true): Transposition = {
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
          (score, score),
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
          var a = alpha
          for {
            p <- game.generateMoves()
            if game.playMove(p, 1)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, a, beta, false)
            best = Math.max(best, t.score)
            a = Math.max(a, best)
            game.undoMove(p, 1)
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
            p <- game.generateMoves()
            if game.playMove(p, 2)
          } {
            val t = alphaBetaWithMem(statuses, game, depth + 1, alpha, b, true)
            best = Math.min(best, t.score)
            b = Math.max(b, best)
            game.undoMove(p, 2)
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