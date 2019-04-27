package game

import scala.annotation.tailrec
import scala.collection.immutable.NumericRange

/**
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {

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
    val bMin: Short = Math.min(n - j, m - i).toShort
    lazy val stopU = Math.min(k, bMin)
    lazy val stopD = Math.min(i, j) + 1

    @tailrec
    def foldDownRight(acc: Int, offset: Short): Int = {
      if (offset == stopU) acc
      else if (board((i + offset, j + offset)) == lastPlayer) foldDownRight(acc + 1, (offset + 1).toShort)
      else acc
    }

    @tailrec
    def foldUpLeft(acc: Int, offset: Short): Int = {
      if (offset == stopD) acc
      else if (board((i - offset, j - offset)) == lastPlayer) foldUpLeft(acc + 1, (offset + 1).toShort)
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
      if (depth == 0 || i < 0 || j>=n || board((i, j)) != lastPlayer) acc
      else foldUpRight(acc + 1, i - 1, j + 1, depth - 1)
    }

    @tailrec
    def foldDownLeft(acc: Int, i: Int, j: Int, depth: Int): Int = {
      if (depth == 0 || i >=m || j < 0 || board((i, j)) != lastPlayer) acc
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
    def foldDown(acc: Int, i: Short, stop: Int): Int = {
      if (i == stop) acc
      else if (board((i, j)) == lastPlayer) foldDown(acc + 1, (i + 1).toShort, stop)
      //      else foldDown(acc, i + 1, stop)
      else acc
    }

    @tailrec
    def foldUp(acc: Int, i: Short, stop: Int): Int = {
      if (i < stop) acc
      else if (board((i, j)) == lastPlayer) foldUp(acc + 1, (i - 1).toShort, stop)
      //      else foldUp(acc, i - 1, stop)
      else acc
    }

    if (LookUps.cols(j)(LookUps.lastPlayerIdx) >= k) {

      val countD = foldDown(0, (i + 1).toShort, Math.min(m, i + k))
      val countU = foldUp(0, (i - 1).toShort, Math.max(0, i - k))

      if (countD + countU >= k1) return lastPlayer
    }

    0
  }

  protected def scoreRow(): Int = {
    val (i, j) = lastMove

    @tailrec
    def foldRight(acc: Int, j: Short, stop: Int): Int = {
      if (j == stop) acc
      else if (board((i, j)) == lastPlayer) foldRight(acc + 1, (j + 1).toShort, stop)
      //      else foldRight(acc, j + 1, stop)
      else acc
    }

    @tailrec
    def foldLeft(acc: Int, j: Short, stop: Int): Int = {
      if (j < stop) acc
      else if (board((i, j)) == lastPlayer) foldLeft(acc + 1, (j - 1).toShort, stop)
      //      else foldLeft(acc, j - 1, stop)
      else acc
    }

    if (LookUps.rows(i)(LookUps.lastPlayerIdx) >= k) {
      // possible win
      val countR = foldRight(0, (j + 1).toShort, Math.min(n, j + k))
      val countL = foldLeft(0, (j - 1).toShort, Math.max(0, j - k))

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
        print(s" ${value(board(i, j))} |")
      }

      println(s" ${value(board(i, n - 1))}")
    }

    println()
  }
}
