package game

import scala.collection.immutable.NumericRange
//import scala.util.control.Breaks._

final class BoardTicTacToe extends BoardMNK(3, 3, 3) {

  protected def scoreRows(): Int = {
    //    for (i <- NumericRange[Short](0, m, 1)) {
    //      val s = scoreRow(i)
    //      if (s > 0) {
    //        return s
    //      }
    //    }
    //    0

    scoreRow(0) + scoreRow(1) + scoreRow(2)
  }

  protected def scoreCols(): Int = {
    scoreCol(0) + scoreCol(1) + scoreCol(2)
  }

  protected def scoreRow(row: Short): Int = {
    if (board(row)(0) == board(row)(1) && board(row)(0) == board(row)(2)) {
      board(row)(0)
    } else {
      0
    }
  }

  protected def checkWinRows(): Boolean = {
    for {
      i <- 0 until m
      if scoreRow(i.toShort) > 0
    } {
      return true
    }

    false
  }

  /**
    *
    * @param col
    * @return 0 none, or player wins
    */
  protected def scoreCol(col: Short): Int = {
    if (board(0)(col) == board(1)(col) && board(0)(col) == board(2)(col)) {
      board(0)(col)
    } else {
      0
    }
  }

  protected def checkWinCols(): Boolean = {
    for {
      j <- 0 until n
      if scoreCol(j.toShort) > 0
    } {
      return true
    }

    false
  }

  def ended(): Boolean = {
    if (!checkWin()) {
      for {
        i <- 0 until m
        j <- 0 until n
        if board(i)(j) == 0
      } {
        return false
      }
    }

    true
  }

  /**
    * @todo cache the current board state for the wining, optimizing the method. for now is recomputing everytime,
    *       *       would be better store with a delta change for each move.
    * @return
    */

  protected def checkWin(): Boolean = checkWinRows() || checkWinCols() || checkWinDiagonals()

  def score(): Int = {
    val score = scoreRows() + scoreCols() + scoreDiagTL() + scoreDiagBR()
    score match {
      case 2 => -1
      case 1 => 1
      case 0 => 0
      case x => throw new IllegalStateException(s"score value $x is not valid")
    }
  }


  // --------------------------------------------------------------
  //---------------------------------------------------------------
  //---------------------------------------------------------------
  //---------------------------------------------------------------

  def display(): Unit = {
    def value(p: Byte): Char = {
      p match {
        case 0 => '_'
        case 1 => 'X'
        case 2 => 'O'
      }
    }

    for (i <- 0 until m) {
      for (j <- 0 until n - 1) {
        print(s" ${value(board(i)(j))} |")
      }
      println(s" ${value(board(i)(n - 1))}")
    }

    println()
  }

  protected def scoreDiagTL(): Int = {
    if (board(0)(0) == board(1)(1) && board(0)(0) == board(2)(2)) {
      board(0)(0)
    } else {
      0
    }
  }

  protected def scoreDiagBR(): Int = {
    if (board(2)(0) == board(1)(1) && board(2)(0) == board(0)(2)) {
      board(2)(0)
    } else {
      0
    }
  }

  protected def checkWinDiagonals(): Boolean = {
    scoreDiagTL() > 0 || scoreDiagBR() > 0
  }
}
