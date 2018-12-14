import game.{BoardMNK, BoardTicTacToe, Transposition, TranspositionTable}

package object ai {

  object Stats {
    protected var _totalCalls: Int = 0

    def totalCalls = _totalCalls

    def totalCalls_=(v: Int): Unit = _totalCalls = v
  }

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

  def alphaBeta(game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    Stats.totalCalls += 1
    if (game.ended()) {
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

  def alphaBetaWithMem(statuses: TranspositionTable, game: BoardMNK, depth: Int = 0, alpha: Double = Double.MinValue, beta: Double = Double.MaxValue, maximizingPlayer: Boolean = true): Double = {
    Stats.totalCalls += 1

    val transposition = statuses.get(game.board)

    if(transposition.isDefined) {
      return transposition.get.score
    }

    if (game.ended()) {
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
        best = Math.max(best, alphaBetaWithMem(statuses, game, depth + 1, a, beta, false))
        //            val abm = alphaBetaWithMem(states, game, depth + 1, a, beta, false)
        //            states.add(game.board, Transposition(abm, a, beta))
        //            best = Math.max(best, abm)
        a = Math.max(a, best)
        statuses.add(game.board, Transposition(best, a, beta, 1))
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

        best = Math.min(best, alphaBetaWithMem(statuses, game, depth + 1, alpha, b, true))

        //            val abm = alphaBetaWithMem(states, game, depth + 1, alpha, b, true)
        //            states.add(game.board, Transposition(abm, alpha, b))
        //            best = Math.min(best, abm)
        b = Math.min(b, best)
        statuses.add(game.board, Transposition(best, alpha, b, 2))
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
