import game.BoardTicTacToe

import scala.util.control.Breaks

package object ai {

  def minimax(game: BoardTicTacToe, isMaximizingPlayer: Boolean): Int = {
    if (game.ended()) {
      return game.score()
    }

    if (isMaximizingPlayer) {
      var best = Int.MinValue
      for {
        i <- 0 until game.m
        j <- 0 until game.n
        if game.board(i)(j) == 0
      } {
        val (is, js) = (i.toShort, j.toShort)
        game.playMove(is, js, 1)
        best = Math.max(best, minimax(game, false))
        game.undoMove(is, js)
      }

      best
    } else {
      var best = Int.MaxValue
      for {
        i <- 0 until game.m
        j <- 0 until game.n
        if game.board(i)(j) == 0
      } {
        val (is, js) = (i.toShort, j.toShort)
        game.playMove(is, js, 2)
        best = Math.min(best, minimax(game, true))
        game.undoMove(is, js)
      }

      best
    }
  }

  def negamax(game: BoardTicTacToe, color: Byte): Int = {
    require(color == 1 || color == -1)
    if (game.ended()) {
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

  def negamaxNextMove(game: BoardTicTacToe, color: Byte): (Int, Short, Short) = {
    require(color == 1 || color == -1)

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

  def alphaBeta(game: BoardTicTacToe, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue, maximizingPlayer: Boolean): Int = {
    if (game.ended()) {
//      return game.score()
      return game.score() * (1/(depth + 1))
    }

    if (maximizingPlayer) {
      var best = Int.MinValue
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
      var best = Int.MaxValue
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
}
