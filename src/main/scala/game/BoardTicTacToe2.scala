package game

import cats.implicits._

/**
  * Used for testing and benchmarking.
  */
class BoardTicTacToe2 extends BoardTicTacToe {

  protected def scoreRow(row: Short): Int = {
    if (board((row, 0)) === board((row, 1)) && board((row, 0)) === board((row, 2))) board((row, 0))
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (board((0, col)) === board((1, col)) && board((0, col)) === board((2, col))) board((0, col))
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (board((0, 0)) === board((1, 1)) && board((0, 0)) === board((2, 2))) _board(0)(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (board((2, 0)) === board((1, 1)) && board((2, 0)) === board((0, 2))) board((2, 0))
    else 0
  }

  override def gameEnded(): Boolean = freePositions === 0 || checkWin()

  override protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
  }
}
