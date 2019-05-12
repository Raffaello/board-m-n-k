package game

/**
  * TODO: can be deleted. It is used for testing and benchmarking.
  * @deprecated
  */
class BoardTicTacToe extends BoardMNK(3, 3, 3) {
  protected def scoreRow(row: Short): Int = {
    if (_board(row)(0) == _board(row)(1) && _board(row)(0) == _board(row)(2)) {
      _board(row)(0)
    } else {
      0
    }
  }

  protected def scoreCol(col: Short): Int = {
    if (_board(0)(col) == _board(1)(col) && _board(0)(col) == _board(2)(col)) {
      _board(0)(col)
    } else {
      0
    }
  }

  protected def scoreDiagsTL(): Int = {
    if (_board(0)(0) == _board(1)(1) && _board(0)(0) == _board(2)(2)) {
      _board(0)(0)
    } else {
      0
    }
  }

  protected def scoreDiagsBR(): Int = {
    if (_board(2)(0) == _board(1)(1) && _board(2)(0) == _board(0)(2)) {
      _board(2)(0)
    } else {
      0
    }
  }

  // TODO: can be removed
  override def gameEnded(): Boolean = {
    val res = freePositions == 0 ||
      checkWin() ||
      !_board.flatten.contains(0)
    res
  }

  // TODO: can be removed
  override protected def checkWin(): Boolean = {
    if (scoreDiagsTL() > 0 || scoreDiagsBR() > 0) true
    else {
//      mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
      for {
        i <- mIndices
        if scoreRow(i) > 0 || scoreCol(i) > 0
      } {
            return true
      }

      false
    }
  }
}
