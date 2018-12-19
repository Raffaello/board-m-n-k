package game

import scala.annotation.tailrec
import scala.collection.immutable.NumericRange

/**
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {

  val nkDiff: Short = (n - k).toShort
  val mkDiff: Short = (m - k).toShort
  val k1: Short = (k - 1).toShort

  protected def scoreRows(): Int = {
    for (i <- NumericRange[Short](0, m, 1)) {
      val s = scoreRow(i)
      if (s > 0) return s
    }

    0
  }

  protected def scoreCols(): Int = {
    for (j <- NumericRange[Short](0, n, 1)) {
      val s = scoreCol(j)
      if (s > 0) return s
    }

    0
  }

  override def score(): Int = {
    if (LookUps.won) lastPlayer match {
      case 2 => -1
      case 1 => 1
      case _ => ???
    } else 0
  }

  /*override*/ def score_(): Int = {
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
    def cmpTail(h: Byte, i: Int, start: Int): Int = {
      if (start == k) board(i)(col)
      else {
        if (h != board(i + start)(col + start)) 0
        else cmpTail(h, i, start + 1)
      }
    }

    if (n - col >= k) {
      for {
        i <- NumericRange.inclusive(0, mkDiff, 1)
        if board(i)(col) > 0
      } {
        val h = board(i)(col)
        val r = cmpTail(h, i, 1)
        if (r > 0) return r
      }
    }

    0
  }

  protected def scoreDiagsTL(): Int = {
    for (i <- NumericRange.inclusive[Short](0, nkDiff, 1)) {
      val s = scoreDiagTL(i)
      if (s > 0) return s
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
      row <- NumericRange(k1, m, 1)
      if board(row)(col) > 0
    } {
      val h = board(row)(col)
      val r = cmpTail(h, row, 1, k)
      if (r > 0) return r
    }

    0
  }

  protected def scoreDiagsBR(): Int = {
    for (j <- NumericRange.inclusive[Short](0, nkDiff, 1)) {
      val s = scoreDiagBR(j)
      if (s > 0) return s
    }

    0
  }

  protected def scoreCol(col: Short): Int = {
    @tailrec
    def cmp(i: Int, j: Int, col: Short, h: Byte): Int = {
      if (j == k) h
      else if (h != board(i + j)(col)) 0
      else cmp(i, j + 1, col, h)
    }

    for (i <- NumericRange.inclusive(0, mkDiff, 1)) {
      val h = board(i)(col)
      if (cmp(i, 1, col, h) > 0) return h
    }

    0
  }

  protected def scoreRow(row: Short): Int = {
    val (i, j) = lastMove

    @tailrec
    def foldRight(acc: Int, j: Int, stop: Int): Int = {
      if (j == stop) acc
      else if(board(i)(j) == lastPlayer) foldRight(acc + 1, j + 1, stop)
      else foldRight(acc, j + 1, stop)
    }
    @tailrec
    def foldLeft(acc: Int, j: Int, stop: Int): Int = {
      if (j < stop) acc
      else if(board(i)(j) == lastPlayer) foldLeft(acc + 1, j - 1, stop)
      else foldLeft(acc, j - 1, stop)
    }

    if (LookUps.rows(i)(LookUps.lastPlayerIdx) >= k) {
      // possible win
      val countR = foldRight(0, j+1, Math.min(n, j+k))
      val countL = foldLeft(0, j-1, Math.max(0, j-k))

      if(countR+countL >=k1) return lastPlayer
    }

    0
  }

  protected def scoreRowOld(row: Short): Int = {
    def innerLoop(i: Int, h: Byte): Int = {
      for (j <- i + 1 until i + k) {
        if (board(row)(j) != h) return 0
      }

      h
    }

    for (i <- NumericRange.inclusive(0, nkDiff, 1)) {
      val h = board(row)(i)
      if (h > 0 && innerLoop(i, h) > 0) return h
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

  override protected def checkWin(): Boolean = {
    LookUps.won = checkWinRows() || checkWinCols() || checkWinDiagonals()
    LookUps.won
  }

  //  override def gameEnded(depth: Int): Boolean = {
  //    if (depth < minWinDepth) false
  //    else if (freePositions == 0) true
  //    else checkWin()
  //  }

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
