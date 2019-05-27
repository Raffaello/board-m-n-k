package game

import scala.collection.immutable.NumericRange
import cats.implicits._

class BoardMNKP(boardMNSize: BoardMNSize, k: Short, val numPlayers: Byte) extends BoardMN(boardMNSize) with Board2dArray with BoardDepthAware {
  require(k <= m || k <= n)
  require(numPlayers >= 2)

  protected val k1: Short = (k - 1).toShort
  protected val nkDiff: Short = (n - k).toShort
  protected val mkDiff: Short = (m - k).toShort

  protected val mkDiffIncIndices: NumericRange.Inclusive[Int] = NumericRange.inclusive(0, mkDiff, 1)
  protected val nkDiffIncIndices: NumericRange.Inclusive[Short] = NumericRange.inclusive[Short](0, nkDiff, 1)
  protected val k1mIndices = NumericRange(k1, m, 1)

  protected var _lastPlayer: Byte = numPlayers
  final val minWinDepth: Int = numPlayers*k1+1//(numPlayers * k) - (numPlayers-1) // np*(k - 1)+1

  def lastPlayer: Byte = this._lastPlayer

  def playMove(position: Position, player: Byte): Boolean = {
    require(player >= 1 && player <= numPlayers)
    if (board(position) > 0) false
    else {
      board_=(position)( player)
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  def undoMove(position: Position, player: Byte): Boolean = {

    if (board(position) === player) {
      board_=(position)(0)
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
}
