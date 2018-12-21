import game._

import scala.collection.immutable.NumericRange

package object ai {
  case class Transposition(score: Int, depth: Int, alpha: Int, beta: Int, isMaximizing: Boolean)
  case class TranspositionOld(score: Double, depth: Int, alpha: Double, beta: Double, isMaximizing: Boolean)

  type AB = (Int, Int)
  type ABScore = (AB, Int)
  type ABStatus = (AB, Status)

  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  /**
    * @deprecated
    */
  def minimax(game: BoardMNK, isMaximizingPlayer: Boolean): Int = {
    def minMaxLoop(maximizing: Boolean): Int = {
      var best: Int = 0
      var cmp: (Int, Int) => Int = null
      var player: Byte = 0

      Stats.totalCalls += 1
      if (maximizing) {
        best = Int.MinValue
        cmp = Math.max _
        player = 1
      }
      else {
        best = Int.MaxValue
        cmp = Math.min _
        player = 2
      }

      for {
        i <- game.mIndices
        j <- game.nIndices
        if game.playMove((i, j), player)
      } {
        best = cmp(best, minimax(game, !maximizing))
        game.undoMove(i, j)
      }

      best
    }

    if (game.gameEnded(game.minWinDepth)) {
      return game.score()
    }

    minMaxLoop(isMaximizingPlayer)
  }

  /**
    * @deprecated
    */
  def negamax(game: BoardMNK, color: Byte): Int = {
//    require(color == 1 || color == -1)
    if (game.gameEnded(game.minWinDepth)) {
      return color * game.score()
    }

    Stats.totalCalls += 1
    var value = Int.MinValue
    var player = color
    if (player == -1) player = 2
    for {
      i <- game.mIndices
      j <- game.nIndices
      if  game.playMove((i, j), player)
    } {
      value = Math.max(value, -negamax(game, (-color).toByte))
      game.undoMove(i, j)
    }

    value
  }

  /**
    * @deprecated
    */
  def negamaxNextMove(game: BoardMNK, color: Byte): (Int, Short, Short) = {
//    require(color == 1 || color == -1)

    if (game.gameEnded(game.minWinDepth)) {
      return (color * game.score(), -1, -1)
    }

    var value = Int.MinValue
    var ibest: Short = -1
    var jbest: Short = -1
    var player = color
    if (player == -1) player = 2
    for {
      i <- game.mIndices
      j <- game.nIndices
      if game.playMove((i, j), player)
    } {
      val newValue = -negamax(game, (-color).toByte)
      if (value < newValue) {
        value = newValue
        ibest = i
        jbest = j
      }
      game.undoMove(i, j)
    }

    (value, ibest, jbest)
  }

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
        game.undoMove(i, j)
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
        game.undoMove(i, j)
        if (alpha >= b) {
          return best
        }
      }

      best
    }
  }

  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): (Double, Short, Short, Double, Double) = {

    var ibest: Short = -1
    var jbest: Short = -1

    if (game.gameEnded(depth)) {
      return (game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0))), ibest, jbest, alpha, beta)
    }

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
//      game.playMove(is, js, player)
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

      game.undoMove(i, j)
      if (a >= beta) {
        return (best, ibest, jbest, a, b)
      }
    }

    (best, ibest, jbest, a, b)
  }

  /**
    * @deprecated
    */
  def alphaBetaWithMemOld(statuses: TranspositionTableOld, game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): TranspositionOld = {

    val transposition = statuses.get(game.getBoard())

    if (transposition.isDefined) {
      Stats.cacheHits += 1
      return transposition.get
    }

    if (game.gameEnded(depth)) {
      val score = game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
      val t = TranspositionOld(
        score,
        depth,
        score,
        score,
        maximizingPlayer
      )

      statuses.add(game.getBoard(), t)
      return t
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
        val t = alphaBetaWithMemOld(statuses, game, depth + 1, a, beta, false)
        best = Math.max(best, t.score)
        a = Math.max(a, best)
        game.undoMove(i, j)
        if (a >= beta) {
          return t
        }
      }

      val t = TranspositionOld(best, depth, a, beta, maximizingPlayer)
      statuses.add(game.getBoard(), t)
      t
    } else {
      var best = Double.MaxValue
      var b = beta
      for {
        i <- game.mIndices
        j <- game.nIndices
        if game.playMove((i, j), 2)
      } {
        val t = alphaBetaWithMemOld(statuses, game, depth + 1, alpha, b, true)
        best = Math.min(best, t.score)
        b = Math.max(b, best)
        game.undoMove(i, j)
        if (alpha >= b) {
          return t
        }
      }

      val t = TranspositionOld(best, depth, alpha, b, maximizingPlayer)
      statuses.add(game.getBoard(), t)
      t
    }
  }

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue, maximizingPlayer: Boolean = true): Transposition = {

    val transposition = statuses.get()

    if (transposition.isDefined) {
      Stats.cacheHits += 1
      return transposition.get
    }

    if (game.gameEnded(depth)) {
      val s = game.score()
      val score = (Math.round(s + (Math.signum(s) * (1.0 / (depth + 1.0)))) * 1000).toInt
      val t = Transposition(
        score,
        depth,
        score,
        score,
        maximizingPlayer
      )

      statuses.add(t)
      return t
    }

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
        game.undoMove(i, j)
        if (a >= beta) {
          return t
        }
      }

      val t = Transposition(best, depth, a, beta, maximizingPlayer)
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
        game.undoMove(i, j)
        if (alpha >= b) {
          return t
        }
      }

      val t = Transposition(best, depth, alpha, b, maximizingPlayer)
      statuses.add(t)
      t
    }
  }
}
