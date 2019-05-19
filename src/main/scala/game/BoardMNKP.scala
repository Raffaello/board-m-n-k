package game

import scala.collection.immutable.NumericRange
import cats.implicits._

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(m: Short, n: Short, k: Short, val numPlayers: Byte) extends BoardMN(m, n) {
  require(k <= m || k <= n)
  require(numPlayers >= 2)

  protected val k1: Short = (k - 1).toShort
  protected val nkDiff: Short = (n - k).toShort
  protected val mkDiff: Short = (m - k).toShort

  protected val mkDiffIncIndices: NumericRange.Inclusive[Int] = NumericRange.inclusive(0, mkDiff, 1)
  protected val nkDiffIncIndices: NumericRange.Inclusive[Short] = NumericRange.inclusive[Short](0, nkDiff, 1)
  protected val k1mIndices = NumericRange(k1, m, 1)

  protected var _lastPlayer: Byte = numPlayers
  protected val minWinDepth: Int = 2 * k1 //(2 * k) - 2 // 2*(k-1) // 2*k1 // zero-based depth require to subtract 1 extra more
  protected var _depth: Int = 0

  def depth: Int = _depth

  def lastPlayer: Byte = this._lastPlayer

  def playMove(position: Position, player: Byte): Boolean = {
    require(player >= 1 && player <= numPlayers)
    val (row, col) = position
    if (_board(row)(col) > 0) false
    else {
      _board(row)(col) = player
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  def undoMove(position: Position, player: Byte): Boolean = {
    val (i, j) = position

    if (_board(i)(j) === player) {
      _board(i)(j) = 0
      freePositions += 1
      _depth -= 1
      true
    } else false
  }

  // last player win? 1
  // last player lost? -1
  // else 0
  override def score(): Int = ???

  // check if board has k in a row of last player then return true otherwise false
  // create a general method to check player p if won.
  protected def checkWin(): Boolean = ???


  override def gameEnded(): Boolean = gameEnded(_depth)

  /**
    * TODO: not sure is well designed here,
    * every call is calling gameEnded to check, and if depth is not enough
    * would re-set every time the lookups won Some(false).
    * It is used just for the score function mainly
    *
    * TODO: reconsider the Lookups.won Option[Boolean] for is usage case.
    */
  def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }

  def opponent(player: Byte): Byte = {
    require(player >= 1 && player <= numPlayers)
    ((player % numPlayers) + 1).toByte
  }

  def nextPlayer(): Byte = opponent(_lastPlayer)

  override def display(): String = ???
}
