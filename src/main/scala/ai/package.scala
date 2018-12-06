import game.{BoardMNK, BoardTicTacToe}

package object ai {


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
        game.playMove(is, js, player)
        best = cmp(best, minimax(game, !maximizing))
        game.undoMove(is, js)
      }

      best
    }

    if (game.ended()) {
      return game.score()
    }

    minMaxLoop(isMaximizingPlayer)
  }

  /**
    * Cannot start from color -1 or the last move just won't be computed correctly
    *
    * @param game
    * @param color
    * @return
    */
//  def negamax(game: BoardTicTacToe, color: Byte): Int = {
//    require(color == 1 || color == -1)
//    if (game.ended()) {
//      return color * game.score()
//    }
//
//    var value = Int.MinValue
//    var player = color
//    if (player == -1) player = 2
//    for {
//      i <- 0 until game.m
//      j <- 0 until game.n
//      if game.board(i)(j) == 0
//    } {
//      val (is, js) = (i.toShort, j.toShort)
//      game.playMove(is, js, player)
//      value = Math.max(value, -negamax(game, (-color).toByte))
//      game.undoMove(is, js)
//    }
//
//    value
//  }

//  def negamaxNextMove(game: BoardTicTacToe, color: Byte): (Int, Short, Short) = {
//    require(color == 1 || color == -1)
//
//    if (game.ended()) {
//      return (color * game.score(), -1, -1)
//    }
//
//    var value = Int.MinValue
//    var ibest: Short = -1
//    var jbest: Short = -1
//    var player = color
//    if (player == -1) player = 2
//    for {
//      i <- 0 until game.m
//      j <- 0 until game.n
//      if game.board(i)(j) == 0
//    } {
//      val (is, js) = (i.toShort, j.toShort)
//      game.playMove(is, js, player)
//      val newValue = -negamax(game, (-color).toByte)
//      if (value < newValue) {
//        value = newValue
//        ibest = is
//        jbest = js
//      }
//      game.undoMove(is, js)
//    }
//
//    (value, ibest, jbest)
//  }

  def negamax(game: BoardMNK, color: Byte): Int = {
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
  def negamaxNextMove(game: BoardMNK, color: Byte): (Int, Short, Short) = {
    require(color == 1 || color == -1)

    if (game.ended()) {
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

//  def alphaBeta(game: BoardTicTacToe, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean): Double = {
//    if (game.ended()) {
////      return game.score()
//      return game.score() * (1.0/(depth + 1))
//    }
//
//    if (maximizingPlayer) {
//      var best = Double.MinValue
//      var a = alpha
//      for {
//        i <- 0 until game.m
//        j <- 0 until game.n
//        if game.board(i)(j) == 0
//      } {
//        val (is, js) = (i.toShort, j.toShort)
//        game.playMove(is, js, 1)
//        best = Math.max(best, alphaBeta(game, depth + 1, a, beta, false))
//        a = Math.max(a, best)
//        game.undoMove(is, js)
//        if (a >= beta) {
//          return best
//        }
//      }
//
//      best
//    } else {
//      var best = Double.MaxValue
//      var b = beta
//      for {
//        i <- 0 until game.m
//        j <- 0 until game.n
//        if game.board(i)(j) == 0
//      } {
//        val (is, js) = (i.toShort, j.toShort)
//        game.playMove(is, js, 2)
//        best = Math.min(best, alphaBeta(game, depth + 1, alpha, b, true))
//        b = Math.min(b, best)
//        game.undoMove(is, js)
//        if (alpha >= b) {
//          return best
//        }
//      }
//
//      best
//    }
//  }
//  def alphaBetaNextMove(game: BoardTicTacToe, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): (Double, Short, Short, Double, Double) = {
//
//    var ibest: Short = -1
//    var jbest: Short = -1
//
//    if (game.ended()) {
//      return (game.score() * (1 / (depth + 1)), ibest, jbest, alpha, beta)
//    }
//
//    var best: Double = 0.0
//    var player: Byte = 0
//    var a = alpha
//    var b = beta
//
//    if (maximizingPlayer) {
//      best = Double.MinValue
//      player = 1
//    }
//    else {
//      best = Double.MaxValue
//      player = 2
//    }
//    for {
//      i <- 0 until game.m
//      j <- 0 until game.n
//      if game.board(i)(j) == 0
//    } {
//      val (is, js) = (i.toShort, j.toShort)
//      game.playMove(is, js, player)
//      val newBest = alphaBeta(game, depth + 1, a, b, !maximizingPlayer)
//
//      if (maximizingPlayer) {
//        if (newBest > best) {
//          best = newBest
//          ibest = is
//          jbest = js
//        }
//        a = Math.max(a, best)
//      } else {
//        if (newBest < best) {
//          best = newBest
//          ibest = is
//          jbest = js
//        }
//
//        b = Math.min(b, best)
//      }
//
//      game.undoMove(is, js)
//      if (a >= beta) {
//        return (best, ibest, jbest, a, b)
//      }
//    }
//
//    (best, ibest, jbest, a, b)
//  }

  def alphaBeta(game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean): Double = {
    if (game.ended()) {
      //      return game.score()
      return game.score() * (1.0/(depth + 1))
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
  def alphaBetaNextMove(game: BoardMNK, depth: Int = 0, alpha: Double, beta: Double, maximizingPlayer: Boolean): (Double, Short, Short, Double, Double) = {

    var ibest: Short = -1
    var jbest: Short = -1

    if (game.ended()) {
      return (game.score() * (1 / (depth + 1)), ibest, jbest, alpha, beta)
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
