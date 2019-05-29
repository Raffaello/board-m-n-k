package game

import cats.implicits._
import game.types.Position

/**
  * Used for testing and benchmarking.
  */
class BoardTicTacToe2 extends BoardTicTacToe {

  protected def scoreRow(row: Short): Int = {
    if (boardPlayer(Position(row, 0)) === boardPlayer(Position(row, 1)) && boardPlayer(Position(row, 0)) === boardPlayer(Position(row, 2))) boardPlayer(Position(row, 0))
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (boardPlayer(Position(0, col)) === boardPlayer(Position(1, col)) && boardPlayer(Position(0, col)) === boardPlayer(Position(2, col))) boardPlayer(Position(0, col))
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (boardPlayer(Position(0, 0)) === boardPlayer(Position(1, 1)) && boardPlayer(Position(0, 0)) === boardPlayer(Position(2, 2))) board(0)(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (boardPlayer(Position(2, 0)) === boardPlayer(Position(1, 1)) && boardPlayer(Position(2, 0)) === boardPlayer(Position(0, 2))) boardPlayer(Position(2, 0))
    else 0
  }

  override def gameEnded(): Boolean = freePositions === 0 || checkWin()

  override protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
  }
}
