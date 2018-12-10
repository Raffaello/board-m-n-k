package game

/**
  * Speed-up optimization on TicTacToe specific case.
  */
final class BoardTicTacToe extends BoardMNK(3, 3, 3) {

  override protected def scoreRow(row: Short): Int = {
    if (board(row)(0) == board(row)(1) && board(row)(0) == board(row)(2)) {
      board(row)(0)
    } else {
      0
    }
  }
  /**
    *
    * @param col
    * @return 0 none, or player wins
    */
  override protected def scoreCol(col: Short): Int = {
    if (board(0)(col) == board(1)(col) && board(0)(col) == board(2)(col)) {
      board(0)(col)
    } else {
      0
    }
  }

  /**
    * @todo cache the current board state for the wining, optimizing the method. for now is recomputing everytime,
    *       *       would be better store with a delta change for each move.
    * @return
    */

//  protected def checkWin(): Boolean = checkWinRows() || checkWinCols() || checkWinDiagonals()

  override def score(): Int = {
    def evaluate(score: Int): Option[Int] = {
      score match {
        case 2 => Some(-1)
        case 1 => Some(1)
        case 0 => None
        case _ => None
      }
    }
      evaluate(scoreRows())
        .orElse(evaluate(scoreCols()))
        .orElse(evaluate(scoreDiagsTL()))
        .orElse(evaluate(scoreDiagsBR()))
        .getOrElse(0)
  }

  override protected def scoreDiagsTL(): Int = {
    if (board(0)(0) == board(1)(1) && board(0)(0) == board(2)(2)) {
      board(0)(0)
    } else {
      0
    }
  }

  override protected def scoreDiagsBR(): Int = {
    if (board(2)(0) == board(1)(1) && board(2)(0) == board(0)(2)) {
      board(2)(0)
    } else {
      0
    }
  }

  override protected def checkWinDiagonals(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0
  }

  override def stale(): Boolean = false
}
