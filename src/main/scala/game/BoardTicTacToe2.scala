package game

import cats.implicits._

/**
  * Used for testing and benchmarking.
  */
class BoardTicTacToe2 extends BoardTicTacToe {

  protected def scoreRow(row: Short): Int = {
    if (board(Position(row, 0)) === board(Position(row, 1)) && board(Position(row, 0)) === board(Position(row, 2))) board(Position(row, 0))
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (board(Position(0, col)) === board(Position(1, col)) && board(Position(0, col)) === board(Position(2, col))) board(Position(0, col))
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (board(Position(0, 0)) === board(Position(1, 1)) && board(Position(0, 0)) === board(Position(2, 2))) _board(0)(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (board(Position(2, 0)) === board(Position(1, 1)) && board(Position(2, 0)) === board(Position(0, 2))) board(Position(2, 0))
    else 0
  }

  override def gameEnded(): Boolean = freePositions === 0 || checkWin()

  override protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
  }
}
