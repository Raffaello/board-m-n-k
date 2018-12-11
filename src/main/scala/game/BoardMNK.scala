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
    def cmpTail(h:Byte, i:Int, start:Int): Int = {
        if (start == k) {
          board(i)(col)
        } else {
          if (h != board(i+start)(col + start)) {
            0
          } else cmpTail(h, i, start+1)
        }
    }

    if (n-col>=k) {
      for {
        i <- 0 to m - k
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
    def cmpTail(h:Byte, i:Int, start:Int, stop: Int): Int = {
      if (start == stop) {
        board(i)(col)
      } else {
        if (h != board(i-start)(col + start)) {
          0
        } else cmpTail(h, i, start+1, stop)
      }
    }

    for {
      row <- k-1 until m
      if board(row)(col) > 0
    } {
      val h = board(row)(col)
      val r = cmpTail(h, row, 1, k)
      if (r > 0) {
        return r
      }
    }
    0
  }

  protected def scoreDiagsBR(): Int = NumericRange.inclusive[Short](0, (n - k).toShort, 1).map(scoreDiagBR).sum

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
      i <- 0 to m-k
    } {
      val h = board(i)(col)
      if (cmp(i, col, h) > 0) {
        return h
      }

    }

    0
  }

  protected def scoreRow(row: Short): Int = {
    for {
      i <- 0 to n - k
    } {
      val x = board(row).slice(i, i + k)
      if (x.tail.forall(x.head > 0 && _ == x.head)) {
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
