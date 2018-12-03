package game

import scala.collection.immutable.NumericRange

/**
  * @todo it could be generalized for p players....
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {

  protected def scoreRows(): Int = NumericRange[Short](0, m, 1).map(scoreRow).sum

  protected def scoreCols(): Int = (0 until n).foldLeft(0)((a, i) => a + scoreCol(i.toShort))

  def score(): Int = ???


  protected def scoreCol(col: Short): Int = {
    def cmp(i: Int, col: Short, h: Byte): Int = {
      for {
        j <- 1 until k
      } {
        if (h != board(i + j)(col)) {
          return 0
        }
      }

      h
    }

    for {
      i <- 0 until m - k
    } {
      val h = board(i)(col)
      if (cmp(i, col, h) > 0) {
        return h
      }

    }

    0

    //    if (board(0)(col) == board(1)(col) && board(0)(col) == board(2)(col)) {
    //      board(0)(col)
    //    } else {
    //      0
    //    }
  }

  protected def scoreRow(row: Short): Int = {
    board(row).combinations(k).foreach { x =>
      if (x.toSet.size == 1) {
        //        if (x.tail.forall(_ == x.head)) {
        return x.head
      }
    }

    0
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

  protected def checkWinCols(): Boolean = {
    for {
      j <- 0 until n
      if scoreCol(j.toShort) > 0
    } {
      return true
    }

    false
  }

  protected def checkWinDiagonals(): Boolean = ???

  protected def checkWin(): Boolean = checkWinRows() || checkWinCols() || checkWinDiagonals()

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
}
