package game

import scala.annotation.tailrec
import scala.collection.immutable.NumericRange

/**
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {

  protected def scoreRowsOld(): Int = {
    for (i <- mIndices) {
      val s = scoreRowOld(i)
      if (s > 0) return s
    }

    0
  }

  /**
    * @deprecated
    */
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

  /**
    * @deprecated
    */
  protected def scoreColOld(col: Short): Int = {
    @tailrec
    def cmp(i: Int, j: Int, col: Short, h: Byte): Int = {
      if (j == k) h
      else if (h != board(i + j)(col)) 0
      else cmp(i, j + 1, col, h)
    }

    for (i <- mkDiffIncIndices) {
      val h = board(i)(col)
      if (cmp(i, 1, col, h) > 0) return h
    }

    0
  }

  /**
    * @deprecated
    * @return
    */
  protected def scoreColsOld(): Int = {
    for (j <- nIndices) {
      val s = scoreColOld(j)
      if (s > 0) return s
    }

    0
  }

  /**
    * @deprecated
    */
  def scoreOld(): Int = {
    def evaluate(score: Int): Option[Int] = {
      score match {
        case 2 => Some(-1)
        case 1 => Some(1)
        case 0 => None
        case _ => None
      }
    }

    evaluate(scoreRowsOld())
      .orElse(evaluate(scoreColsOld()))
      .orElse(evaluate(scoreDiagsTL()))
      .orElse(evaluate(scoreDiagsBR()))
      .getOrElse(0)
  }

  /**
    * @deprecated
    * @return
    */
  protected def checkWinRowsOld(): Boolean = {
    for {
      i <- mIndices
      if scoreRowOld(i) > 0
    } return true

    false
  }

  /**
    * @deprecated
    * @return
    */
  protected def checkWinColsOld(): Boolean = {
    for {
      j <- nIndices
      if scoreColOld(j) > 0
    } return true

    false
  }

  protected def checkWinDiagonals(): Boolean = scoreDiagsTL() > 0 || scoreDiagsBR() > 0

  /**
    * @deprecated
    * @return
    */
  protected def checkWinOld(): Boolean = {
    checkWinRowsOld() || checkWinColsOld() || checkWinDiagonals()
  }

  // ------------ to do with lookups

  /**
    * @deprecated
    */
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

  /**
    * @deprecated
    */
  protected def scoreDiagsTL(): Int = {
    for (i <- nkDiffIncIndices) {
      val s = scoreDiagTL(i)
      if (s > 0) return s
    }

    0
  }

  /**
    * @deprecated
    */
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

  /**
    * @deprecated
    */
  protected def scoreDiagsBR(): Int = {
    for (j <- nkDiffIncIndices) {
      val s = scoreDiagBR(j)
      if (s > 0) return s
    }

    0
  }

  // -------------------------------------------

  override def score(): Int = {
    if (LookUps.won.getOrElse(checkWin())) lastPlayer match {
      case 2 => -1
      case 1 => 1
      case _ => ???
    } else 0
  }


  /**
    * @TODO checkWinDiagoanls /w lookup
    * @return
    */
  override protected def checkWin(): Boolean = {
    LookUps.won = Some(scoreRow() > 0 || scoreCol() > 0 || scoreDiagSE() > 0 || scoreDiagNE() > 0)
    LookUps.won.get
  }

  protected def scoreDiagSE(): Int = {
    val (i, j) = lastMove
    val bMin = Math.min(n - j, m - i)
    lazy val stopU = Math.min(k, bMin)
    lazy val stopD = Math.min(i, j) + 1

    @tailrec
    def foldDownRight(acc: Int, offset: Int): Int = {
      if (offset == stopU) acc
      else if (board(i + offset)(j + offset) == lastPlayer) foldDownRight(acc + 1, offset + 1)
      else acc
    }

    @tailrec
    def foldUpLeft(acc: Int, offset: Int): Int = {
      if (offset == stopD) acc
      else if (board(i - offset)(j - offset) == lastPlayer) foldUpLeft(acc + 1, offset + 1)
      else acc
    }

    if (Math.min(i, j) + bMin >= k1) {
      val countD = foldDownRight(0, 1)
      val countU = foldUpLeft(0, 1)
      if (countD + countU >= k1) return lastPlayer
    }

    0
  }

  protected def scoreDiagNE(): Int = {
    val (i, j) = lastMove

    @tailrec
    def foldUpRight(acc: Int, i: Int, j: Int, depth: Int): Int = {
      if (depth == 0 || i < 0 || j>=n || board(i)(j) != lastPlayer) acc
      else foldUpRight(acc + 1, i - 1, j + 1, depth - 1)
    }

    @tailrec
    def foldDownLeft(acc: Int, i: Int, j: Int, depth: Int): Int = {
      if (depth == 0 || i >=m || j < 0 || board(i)(j) != lastPlayer) acc
      else foldDownLeft(acc + 1, i + 1, j - 1, depth - 1)
    }

//    if (m - i >= k && n - j >= k) {
    val countD = foldUpRight(0, i - 1, j + 1, k1)
    val countU = foldDownLeft(0, i + 1, j - 1, k1)

    if (countD + countU >= k1) lastPlayer
    else 0
  }

  protected def scoreCol(): Int = {
    val (i, j) = lastMove

    @tailrec
    def foldDown(acc: Int, i: Int, stop: Int): Int = {
      if (i == stop) acc
      else if (board(i)(j) == lastPlayer) foldDown(acc + 1, i + 1, stop)
      //      else foldDown(acc, i + 1, stop)
      else acc
    }

    @tailrec
    def foldUp(acc: Int, i: Int, stop: Int): Int = {
      if (i < stop) acc
      else if (board(i)(j) == lastPlayer) foldUp(acc + 1, i - 1, stop)
      //      else foldUp(acc, i - 1, stop)
      else acc
    }

    if (LookUps.cols(j)(LookUps.lastPlayerIdx) >= k) {

      val countD = foldDown(0, i + 1, Math.min(m, i + k))
      val countU = foldUp(0, i - 1, Math.max(0, i - k))

      if (countD + countU >= k1) return lastPlayer
    }

    0
  }

  protected def scoreRow(): Int = {
    val (i, j) = lastMove

    @tailrec
    def foldRight(acc: Int, j: Int, stop: Int): Int = {
      if (j == stop) acc
      else if (board(i)(j) == lastPlayer) foldRight(acc + 1, j + 1, stop)
      //      else foldRight(acc, j + 1, stop)
      else acc
    }

    @tailrec
    def foldLeft(acc: Int, j: Int, stop: Int): Int = {
      if (j < stop) acc
      else if (board(i)(j) == lastPlayer) foldLeft(acc + 1, j - 1, stop)
      //      else foldLeft(acc, j - 1, stop)
      else acc
    }

    if (LookUps.rows(i)(LookUps.lastPlayerIdx) >= k) {
      // possible win
      val countR = foldRight(0, j + 1, Math.min(n, j + k))
      val countL = foldLeft(0, j - 1, Math.max(0, j - k))

      if (countR + countL >= k1) return lastPlayer
    }

    0
  }

  def display(): Unit = {
    def value(p: Byte): Char = {
      p match {
        case 0 => '_'
        case 1 => 'X'
        case 2 => 'O'
      }
    }

    for (i <- mIndices) {
      for (j <- 0 until n - 1) {
        print(s" ${value(board(i)(j))} |")
      }

      println(s" ${value(board(i)(n - 1))}")
    }

    println()
  }
}
