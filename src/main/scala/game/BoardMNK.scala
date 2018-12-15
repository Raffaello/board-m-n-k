package game

import scala.annotation.tailrec
import scala.collection.immutable.NumericRange

/**
  * @todo it could be generalized for p players....
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {

  val nkDiff: Short = (n - k).toShort
  val mkDiff: Short = (m - k).toShort
  val k1: Short = (k - 1).toShort

  protected def scoreRows(): Int = NumericRange[Short](0, m, 1).map(scoreRow).sum

  protected def scoreCols(): Int = (0 until n).foldLeft(0)((a, i) => a + scoreCol(i.toShort))

  def score(): Int = {
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

  protected def scoreDiagTL(col: Short): Int = {
    @tailrec
    def cmpTail(h: Byte, i: Short, start: Int): Int = {
      if (start == k) {
        board(i)(col)
      } else {
        if (h != board(i + start)(col + start)) {
          0
        } else cmpTail(h, i, start + 1)
      }
    }

    if (n - col >= k) {
      for {
        i <- NumericRange.inclusive[Short](0, mkDiff, 1)
        if board(i)(col) > 0
      } {
        val h = board(i)(col)
        val r = cmpTail(h, i, 1)
        if (r > 0) return r
      }
    }

    0
  }

  protected def scoreDiagsTL(): Int = NumericRange.inclusive[Short](0, (n - k).toShort, 1).map(scoreDiagTL).sum

  protected def scoreDiagBR(col: Short): Int = {
    @tailrec
    def cmpTail(h: Byte, i: Short, start: Int, stop: Int): Int = {
      if (start == stop) {
        board(i)(col)
      } else {
        if (h != board(i - start)(col + start)) 0 else cmpTail(h, i, start + 1, stop)
      }
    }

    for {
      row <- NumericRange[Short](k1, m, 1)
      if board(row)(col) > 0
    } {
      val h = board(row)(col)
      val r = cmpTail(h, row, 1, k)
      if (r > 0) return r
    }

    0
  }

  protected def scoreDiagsBR(): Int = NumericRange.inclusive[Short](0, nkDiff, 1).map(scoreDiagBR).sum

  protected def scoreCol(col: Short): Int = {
    @tailrec
    def cmp(i: Short, j: Short, col: Short, h: Byte): Int = {
      if (j == k) h
      else if (h != board(i + j)(col)) 0
      else cmp(i, (j+1).toShort, col, h)
    }

    for (i <- NumericRange.inclusive[Short](0, mkDiff, 1)) {
      val h = board(i)(col)
      if (cmp(i, 1, col, h) > 0) {
        return h
      }
    }

    0
  }

  protected def scoreRow(row: Short): Int = {
    for {
      i <- NumericRange.inclusive[Short](0, nkDiff, 1)
      x = board(row).slice(i, i + k)
    } {
      val h = x.head
      if (x.tail.forall(h > 0 && _ == h)) return h
    }

    0
  }

  protected def checkWinRows(): Boolean = {
    for {
      i <- NumericRange[Short](0, m, 1)
      if scoreRow(i) > 0
    } return true

    false
  }

  protected def checkWinCols(): Boolean = {
    for {
      j <- NumericRange[Short](0, n, 1)
      if scoreCol(j) > 0
    } return true

    false
  }

  protected def checkWinDiagonals(): Boolean = scoreDiagsTL() > 0 || scoreDiagsBR() > 0

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
