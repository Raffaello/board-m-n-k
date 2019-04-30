package game

import scala.collection.immutable.NumericRange

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n) {
  //  require(k > 2)
  //  require(k <= m || k <= n)
  //  require(numPlayers >= 2)

  val k1: Short = (k - 1).toShort
  val nkDiff: Short = (n - k).toShort
  val mkDiff: Short = (m - k).toShort

  protected val mkDiffIncIndices: NumericRange.Inclusive[Int] = NumericRange.inclusive(0, mkDiff, 1)
  protected val nkDiffIncIndices: NumericRange.Inclusive[Short] = NumericRange.inclusive[Short](0, nkDiff, 1)
  protected val k1mIndices = NumericRange(k1, m, 1)

  protected var lastPlayer: Byte = 0


  object LookUps {
    val rows: Array[Array[Int]] = Array.ofDim[Int](m, numPlayers)
    val cols: Array[Array[Int]] = Array.ofDim[Int](n, numPlayers)
    var lastPlayerIdx: Int = 0
    var won: Option[Boolean] = Some(false)

    def inc(pos: Position, playerIdx: Int): Unit = {
      lastPlayerIdx = playerIdx
      rows(pos._1)(playerIdx) += 1
      assert(rows(pos._1)(playerIdx) <= n)
      cols(pos._2)(playerIdx) += 1
      assert(cols(pos._2)(playerIdx) <= m, s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos -- ${board.flatten.mkString}")
      // TODO DIAGS1 and DIAG2

    }

    def dec(pos: Position, playerIdx: Int): Unit = {
      rows(pos._1)(playerIdx) -= 1
      assert(rows(pos._1)(playerIdx) >= 0)
      cols(pos._2)(playerIdx) -= 1
      assert(cols(pos._2)(playerIdx) >= 0, s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos")
    }
  }

  val minWinDepth: Int = (2 * k) - 2 // 2*(k-1) // 2*k1 // zero-based depth require to subtract 1 extra more

  def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = position
    LookUps.won = None
    if (board(row)(col) > 0) false
    else {
      board(row)(col) = player
      freePositions -= 1
      lastMove = position
      lastPlayer = player
      LookUps.inc(position, player - 1)
      true
    }
  }

  /**
    * TODO: should check if it is not already zero otherwise
    * freePositions should not be incremented
    */
  def undoMove(position: Position, player: Byte): Unit = {
    assert(board(position._1)(position._2) > 0)
    board(position._1)(position._2) = 0
    freePositions += 1
    LookUps.dec(position, player - 1)
    LookUps.won = None
    lastPlayer = player
  }

  override def score(): Int = ???

  protected def checkWin(): Boolean = ???

  /**
    * TODO: not sure is well designed here,
    * every call is calling gameEnded to check, and if depth is not enough
    * would re-set everytime the lookups won Some(false).
    * It is used just for the score function mainly
    *
    * TODO: reconsider the Lookups.won Option[Boolean] for is usage case.
    */
  override def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) {
      LookUps.won = Some(false)
      false
    }
    else if (freePositions == 0) {
      true
    }
    else checkWin()
  }
}
