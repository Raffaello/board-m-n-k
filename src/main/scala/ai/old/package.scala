package ai

import game.{Board, BoardMNK}

/**
  * Used for benchmarks.
  */
package object old {

  case class TranspositionOld(score: Double, depth: Int, alpha: Double, beta: Double, isMaximizing: Boolean)
  trait withGetBoard extends BoardMNK {
    def getBoard(): Board = board
  }

  class BoardMNKwithGetBoard(m: Short, n: Short, k: Short) extends BoardMNK(m, n, k) with withGetBoard {}

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
        game.undoMove((i, j), player)
      }

      best
    }

    if (game.gameEnded(game.minWinDepth)) {
      return game.score()
    }

    minMaxLoop(isMaximizingPlayer)
  }

  /**
    * do not use.
    * Doesn't work as expected.
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
      game.undoMove((i, j), player)
    }

    value
  }

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
      game.undoMove((i, j), player)
    }

    (value, ibest, jbest)
  }

  def alphaBetaWithMem(statuses: old.TranspositionTable, game: withGetBoard, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): TranspositionOld = {

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
        val t = alphaBetaWithMem(statuses, game, depth + 1, a, beta, false)
        best = Math.max(best, t.score)
        a = Math.max(a, best)
        game.undoMove((i, j), 1)
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
        val t = alphaBetaWithMem(statuses, game, depth + 1, alpha, b, true)
        best = Math.min(best, t.score)
        b = Math.max(b, best)
        game.undoMove((i, j), 2)
        if (alpha >= b) {
          return t
        }
      }

      val t = TranspositionOld(best, depth, alpha, b, maximizingPlayer)
      statuses.add(game.getBoard(), t)
      t
    }
  }
}
