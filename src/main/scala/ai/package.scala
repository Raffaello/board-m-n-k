import game.{BoardMNK, BoardMNKP, BoardTicTacToe}

package object ai {

  /**
    * @todo compute valid moves until depth, and choose the best ones, for P players >=2
    * @param game
    * @param depth
    * @param isMaximizingPlayer
    */
  def minimax(game: BoardMNKP, depth: Int, isMaximizingPlayer: Boolean) = {

  }

  def minimax(game: BoardMNK, depth: Int, isMaximizingPlayer: Boolean) = {
    if (depth == 0 || game.ended()) {
      game.score()
    }

  }


  def minimax(game: BoardTicTacToe, isMaximizingPlayer: Boolean): Int = {
//    game.display()
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

    return value
  }

  def negamaxNextMove(game: BoardTicTacToe, color: Byte): (Int, Short, Short) = {
    require(color == 1 || color == -1)

    var value = Int.MinValue
    var ibest:Short = -1
    var jbest:Short = -1
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

  //  def alphaBeta(game: BoardMNK)
}
