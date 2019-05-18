package game

import cats.implicits._

import scala.annotation.tailrec

/**
  * TODO: potentially split in BoardNMK and BoardMNKLookUp (traits)
  *
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, val k: Short) extends BoardMNKPLookUp(m, n, k, 2) {
  require(k > 2)

  final protected def score2players(player: Byte): Int = {
    player match {
      case 2 => -1
      case 1 => 1
      case _ => ??? // could be zero, but should never reach here.
    }
  }

  /**
    * TODO better generalization: score()(player: Byte => score: Int) ???
    * implicit score2players here
    * in the P>2 ??? no implicit but required a function?
    *
    * @return
    */
  override def score(): Int = {
    if (checkWin()) score2players(_lastPlayer)
    else 0
  }

  private[this] def checkScore(): Boolean = scoreRow() > 0 || scoreCol() > 0 || scoreDiagSE() > 0 || scoreDiagNE() > 0

  /**
    * TODO checkWinDiagonals /w lookup
    */
  override protected def checkWin(): Boolean = {
    lookUps.ended match {
      case None =>
        val w = checkScore()
        lookUps.ended = Some(w)
        w
      case Some(x) => x
    }
  }

  /**
    * South-East direction checking: bottom-right to top-left
    */
  protected def scoreDiagSE(): Int = {
    val (i, j) = _lastMove
    val bMin = Math.min(n - j, m - i)
    lazy val stopU = Math.min(k, bMin)
    lazy val stopD = Math.min(i, j) + 1

    @tailrec
    def foldDownRight(acc: Int, offset: Int): Int = {
      if (offset === stopU) acc
      else if (_board(i + offset)(j + offset) === _lastPlayer) foldDownRight(acc + 1, offset + 1)
      else acc
    }

    @tailrec
    def foldUpLeft(acc: Int, offset: Int): Int = {
      if (offset === stopD) acc
      else if (_board(i - offset)(j - offset) === _lastPlayer) foldUpLeft(acc + 1, offset + 1)
      else acc
    }

    if (Math.min(i, j) + bMin >= k1) {
      val countD = foldDownRight(0, 1)
      val countU = foldUpLeft(0, 1)
      if (countD + countU >= k1) _lastPlayer
      else 0
    } else 0
  }

  /**
    * North-East direction checking: bottom-left to top-right
    *
    * @return
    */
  protected def scoreDiagNE(): Int = {
    val (i, j) = _lastMove

    @tailrec
    def foldUpRight(acc: Int, i: Int, j: Int, depth: Int): Int = {
      if (depth === 0 || i < 0 || j >= n || _board(i)(j) =!= _lastPlayer) acc
      else foldUpRight(acc + 1, i - 1, j + 1, depth - 1)
    }

    @tailrec
    def foldDownLeft(acc: Int, i: Int, j: Int, depth: Int): Int = {
      if (depth === 0 || i >= m || j < 0 || _board(i)(j) =!= _lastPlayer) acc
      else foldDownLeft(acc + 1, i + 1, j - 1, depth - 1)
    }

    //    if (m - i >= k && n - j >= k) {
    val countD = foldUpRight(0, i - 1, j + 1, k1)
    val countU = foldDownLeft(0, i + 1, j - 1, k1)

    if (countD + countU >= k1) _lastPlayer
    else 0
  }

  protected def scoreCol(): Int = {
    val (i, j) = _lastMove

    @tailrec
    def foldDown(acc: Int, i: Int, stop: Int): Int = {
      if (i === stop) acc
      else if (_board(i)(j) === _lastPlayer) foldDown(acc + 1, i + 1, stop)
      else acc
    }

    @tailrec
    def foldUp(acc: Int, i: Int, stop: Int): Int = {
      if (i < stop) acc
      else if (_board(i)(j) === _lastPlayer) foldUp(acc + 1, i - 1, stop)
      else acc
    }

    if (lookUps.cols(j)(lookUps.lastPlayerIdx) >= k) {

      val countD = foldDown(0, i + 1, Math.min(m, i + k))
      val countU = foldUp(0, i - 1, Math.max(0, i - k))

      if (countD + countU >= k1) _lastPlayer else 0
    } else 0
  }

  protected def scoreRow(): Int = {
    val (i, j) = _lastMove

    @tailrec
    def foldRight(acc: Int, j: Int, stop: Int): Int = {
      if (j === stop) acc
      else if (_board(i)(j) === _lastPlayer) foldRight(acc + 1, j + 1, stop)
      else acc
    }

    @tailrec
    def foldLeft(acc: Int, j: Int, stop: Int): Int = {
      if (j < stop) acc
      else if (_board(i)(j) === _lastPlayer) foldLeft(acc + 1, j - 1, stop)
      else acc
    }

    if (lookUps.rows(i)(lookUps.lastPlayerIdx) >= k) {
      // possible win
      val countR = foldRight(0, j + 1, Math.min(n, j + k))
      val countL = foldLeft(0, j - 1, Math.max(0, j - k))

      if (countR + countL >= k1) _lastPlayer
      else 0
    } else 0
  }


  override def display(): String = {
    val str: StringBuilder = new StringBuilder()
    val newLine = sys.props("line.separator")

    def value(p: Byte): Char = {
      p match {
        case 0 => '_'
        case 1 => 'X'
        case 2 => 'O'
        case _ => ???
      }
    }

    for (i <- mIndices) {
      for (j <- 0 until n - 1) {
        str ++= s" ${value(_board(i)(j))} |"
      }

      str ++= s" ${value(_board(i)(n - 1))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}
