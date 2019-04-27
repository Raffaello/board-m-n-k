package game

/**
  * Speed-up optimization on TicTacToe specific case.
  */
class BoardTicTacToe extends BoardMNK(3, 3, 3) {

  /** @deprecated */
//  override protected def checkWin(): Boolean = super.checkWinOld()

  /** @deprecated */
//  override def score(): Int = super.scoreOld()

  /**
    * @deprecated
    */
//  override protected def scoreRowOld(row: Short): Int = {
//    if (board(row)(0) == board(row)(1) && board(row)(0) == board(row)(2)) {
//      board(row)(0)
//    } else {
//      0
//    }
//  }

  /**
    * @deprecated
    */
//  override protected def scoreColOld(col: Short): Int = {
//    if (board(0)(col) == board(1)(col) && board(0)(col) == board(2)(col)) {
//      board(0)(col)
//    } else {
//      0
//    }
//  }

//  /**
//    * @deprecated
//    */
//  override protected def scoreDiagsTL(): Int = {
//    if (board(0)(0) == board(1)(1) && board(0)(0) == board(2)(2)) {
//      board(0)(0)
//    } else {
//      0
//    }
//  }

  /**
//    * @deprecated
//    */
//  override protected def scoreDiagsBR(): Int = {
//    if (board(2)(0) == board(1)(1) && board(2)(0) == board(0)(2)) {
//      board(2)(0)
//    } else {
//      0
//    }
//  }
}
