import game._

import scala.collection.immutable.NumericRange

package object ai {

  type AB = (Int, Int)
  type ABScore = (AB, Int)
  type ABStatus = (AB, Status)

  object Stats {
    var totalCalls: Int = 0
  }

  /**
    * @deprecated
    */
  def minimax(game: BoardTicTacToe, isMaximizingPlayer: Boolean): Int = {
    def minMaxLoop(maximizing: Boolean): Int = {
      var best: Int = 0
      var cmp: (Int, Int) => Int = null
      var player: Byte = 0

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
        i <- 0 until game.m
        j <- 0 until game.n
        if game.board(i)(j) == 0
      } {
        val (is, js) = (i.toShort, j.toShort)
        game.playMove((is, js), player)
        best = cmp(best, minimax(game, !maximizing))
        game.undoMove(is, js)
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
    require(color == 1 || color == -1)
    if (game.gameEnded(game.minWinDepth)) {
      return color * game.score()
    }

    var value = Int.MinValue
    var player = color
    if (player == -1) player = 2
    for {
      i <- 0 until game.m
      j <- 0 until game.n
      if game.board(i)(j) == 0
    } {
      val (is, js) = (i.toShort, j.toShort)
      game.playMove(is, js, player)
      value = Math.max(value, -negamax(game, (-color).toByte))
      game.undoMove(is, js)
    }

    value
  }

  /**
    * @deprecated
    */
  def negamaxNextMove(game: BoardMNK, color: Byte): (Int, Short, Short) = {
    require(color == 1 || color == -1)

    if (game.gameEnded(game.minWinDepth)) {
      return (color * game.score(), -1, -1)
    }

    var value = Int.MinValue
    var ibest: Short = -1
    var jbest: Short = -1
    var player = color
    if (player == -1) player = 2
    for {
      i <- 0 until game.m
      j <- 0 until game.n
      if game.board(i)(j) == 0
    } {
      val (is, js) = (i.toShort, j.toShort)
      game.playMove(is, js, player)
      val newValue = -negamax(game, (-color).toByte)
      if (value < newValue) {
        value = newValue
        ibest = is
        jbest = js
      }
      game.undoMove(is, js)
    }

    (value, ibest, jbest)
  }

  def alphaBeta(game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    Stats.totalCalls += 1
    if (game.gameEnded(depth)) {
      //      return game.score()
      //      return game.score() * (1.0/(depth + 1))
      return game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))

    }

    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      for {
        i <- 0 until game.m
        j <- 0 until game.n
        if game.board(i)(j) == 0
      } {
        val (is, js) = (i.toShort, j.toShort)
        game.playMove(is, js, 1)
        best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
        a = Math.max(a, best)
        game.undoMove(is, js)
        if (a >= beta) {
          return best
        }
      }

      best
    } else {
      var best = Double.MaxValue
      var b = beta
      for {
        i <- 0 until game.m
        j <- 0 until game.n
        if game.board(i)(j) == 0
      } {
        val (is, js) = (i.toShort, j.toShort)
        game.playMove(is, js, 2)
        best = Math.min(best, alphaBeta(game, depth + 1, alpha, b, true))
        b = Math.min(b, best)
        game.undoMove(is, js)
        if (alpha >= b) {
          return best
        }
      }

      best
    }
  }

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Transposition = {
    Stats.totalCalls += 1

    val transposition = statuses.get(game.board)

    if (transposition.isDefined) {
      //      val score = alphaBeta(game, depth, alpha, beta, maximizingPlayer)
      //      if (score != transposition.get.score) throw new IllegalStateException(s"score: $score --- transpostion: ${transposition}  -- $maximizingPlayer")
      return transposition.get
    }

    if (game.gameEnded(depth)) {
      //      return game.score()
      //      return game.score() * (1.0/(depth + 1))
      val score = game.score + (Math.signum(game.score()) * (1.0 / (depth + 1.0)))
      val t = Transposition(
        score,
        depth,
        Math.max(alpha, score),
        Math.min(beta, score),
        maximizingPlayer
      )
      statuses.add(game.board, t)
      return t
    }

    if (maximizingPlayer) {
      var best = Double.MinValue
      var a = alpha
      for {
        i <- NumericRange[Short](0, game.m, 1)
        j <- NumericRange[Short](0, game.n, 1)
        if game.board(i)(j) == 0
      } {
        game.playMove(i, j, 1)
        val t = alphaBetaWithMem(statuses, game, depth + 1, a, beta, false)
        best = Math.max(best, t.score)
        a = t.alpha
        game.undoMove(i, j)
        if (a >= beta) {
          return t
        }
      }

      val t = Transposition(best, depth, alpha, beta, maximizingPlayer)
      statuses.add(game.board, t)
      t
    } else {
      var best = Double.MaxValue
      var b = beta
      for {
        i <- NumericRange[Short](0, game.m, 1)
        j <- NumericRange[Short](0, game.n, 1)
        if game.board(i)(j) == 0
      } {
        game.playMove(i, j, 2)
        val t = alphaBetaWithMem(statuses, game, depth + 1, alpha, b, true)
        best = Math.min(best, t.score)
        b = t.beta
        game.undoMove(i, j)
        if (alpha >= b) {
          return t
        }
      }

      val t = Transposition(best, depth, alpha, beta, maximizingPlayer)
      statuses.add(game.board, t)
      t
    }
  }

  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): (Double, Short, Short, Double, Double) = {

    var ibest: Short = -1
    var jbest: Short = -1

    if (game.gameEnded(depth)) {
      //      return (game.score() * (1 / (depth + 1)), ibest, jbest, alpha, beta)
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
      i <- 0 until game.m
      j <- 0 until game.n
      if game.board(i)(j) == 0
    } {
      val (is, js) = (i.toShort, j.toShort)
      game.playMove(is, js, player)
      val newBest = alphaBeta(game, depth + 1, a, b, !maximizingPlayer)

      if (maximizingPlayer) {
        if (newBest > best) {
          best = newBest
          ibest = is
          jbest = js
        }
        a = Math.max(a, best)
      } else {
        if (newBest < best) {
          best = newBest
          ibest = is
          jbest = js
        }

        b = Math.min(b, best)
      }

      game.undoMove(is, js)
      if (a >= beta) {
        return (best, ibest, jbest, a, b)
      }
    }

    (best, ibest, jbest, a, b)
  }

}
