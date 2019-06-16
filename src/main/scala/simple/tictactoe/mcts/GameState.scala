package simple.tictactoe.mcts

import types.Position
import types.implicits.convertIntToShort

class GameState() {
  var board: Array[Int] = Array.ofDim[Int](9)
  var lastPlayer = 2

  def currentPlayer(): Int = 3 - lastPlayer

  def playMove(move: Position): Unit = {
    val i = move.row * 3 + move.col
    assert(board(i) == 0)
    val player = currentPlayer()
    board(i) = player
    lastPlayer = player
  }

  def allRemainingMoves(): IndexedSeq[Position] = {
    for {
      i <- 0 until 3
      j <- 0 until 3
      if board(i * 3 + j) == 0
    } yield Position(i, j)
  }

  // current player?
  // seems not ok
  def result(playerJustMove: Int): Double = {
    val winMoves: Seq[(Int, Int, Int)] = Seq(
      (0, 1, 2), (3, 4, 5), (6, 7, 8), // rows
      (0, 3, 6), (1, 4, 7), (2, 5, 8), // cols
      (0, 4, 8), (2, 4, 6) // diags
    )

    for ((x, y, z) <- winMoves) {
      if (board(x) == board(y) && board(y) == board(z)) {
        if (board(x) == playerJustMove) return 1.0
        else return 0.0
      }
    }

    0.5
  }

  def copy(): GameState = {
    val c = new GameState
    Array.copy(board, 0, c.board, 0, 9)
    c.lastPlayer = this.lastPlayer
    c
  }

  override def toString: String = {
    val str = new StringBuffer()

    for (i <- board.indices) {
      if (i % 3 == 0) str.append("\n")

      str.append(
        board(i) match {
          case 1 => "X"
          case 2 => "O"
          case 0 => "_"
        }
      )
    }
    str.append("\n").toString
  }
}
