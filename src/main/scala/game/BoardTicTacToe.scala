package game

import scala.annotation.tailrec

/**
  * Speed-up optimization on TicTacToe specific case.
  */
class BoardTicTacToe extends BoardMNK(3, 3, 3) {

  protected def checkWinRows(): Boolean = {
    for {
      i <- mIndices
      if scoreRowOld(i) > 0
    } return true

    false
  }

  protected def checkWinCols(): Boolean = {
    for {
      j <- nIndices
      if scoreColOld(j) > 0
    } return true

    false
  }

  protected def checkWinDiagonals(): Boolean = scoreDiagsTL() > 0 || scoreDiagsBR() > 0

  override protected def checkWin(): Boolean = checkWinRows() || checkWinCols() || checkWinDiagonals()

  protected def scoreRows(): Int = {
    for (i <- mIndices) {
      val s = scoreRowOld(i)
      if (s > 0) return s
    }

    0
  }

  protected def scoreColsOld(): Int = {
    for (j <- nIndices) {
      val s = scoreColOld(j)
      if (s > 0) return s
    }

    0
  }

  protected def scoreDiagTL(col: Short): Int = {
    @tailrec
    def cmpTail(h: Byte, i: Int, start: Int): Int = {
      if (start == k) board(i)(col)
      else {
        if (h != board(i + start)(col + start)) 0
        else cmpTail(h, i, start + 1)
      }
    }

    if (n - col >= k) {
      for {
        i <- mkDiffIncIndices
        if board(i)(col) > 0
      } {
        val h = board(i)(col)
        val r = cmpTail(h, i, 1)
        if (r > 0) return r
      }
    }

    0
  }

  protected def scoreDiagBR(col: Short): Int = {
    @tailrec
    def cmpTail(h: Byte, i: Int, start: Int, stop: Int): Int = {
      if (start == stop) board(i)(col)
      else if (h != board(i - start)(col + start)) 0
      else cmpTail(h, i, start + 1, stop)
    }

    for {
      row <- k1mIndices
      if board(row)(col) > 0
    } {
      val h = board(row)(col)
      val r = cmpTail(h, row, 1, k)
      if (r > 0) return r
    }

    0
  }

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
      .orElse(evaluate(scoreColsOld()))
      .orElse(evaluate(scoreDiagsTL()))
      .orElse(evaluate(scoreDiagsBR()))
      .getOrElse(0)
  }

  protected def scoreRowOld(row: Short): Int = {
    if (board(row)(0) == board(row)(1) && board(row)(0) == board(row)(2)) {
      board(row)(0)
    } else {
      0
    }
  }

  protected def scoreColOld(col: Short): Int = {
    if (board(0)(col) == board(1)(col) && board(0)(col) == board(2)(col)) {
      board(0)(col)
    } else {
      0
    }
  }

  protected def scoreDiagsTL(): Int = {
    if (board(0)(0) == board(1)(1) && board(0)(0) == board(2)(2)) {
      board(0)(0)
    } else {
      0
    }
  }

  protected def scoreDiagsBR(): Int = {
    if (board(2)(0) == board(1)(1) && board(2)(0) == board(0)(2)) {
      board(2)(0)
    } else {
      0
    }
  }
}
