package game

import cats.implicits._
import game.boards.{BoardDepthAware, BoardMN, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO Board2fArray trait should not be included here... remove it later.
  * TODO Board2Array has to be a type of boards not a with trait
  */
class BoardMNKP(boardMNSize: BoardMNSize, val k: Short, val numPlayers: Byte) extends BoardMN(boardMNSize)
  with BoardDepthAware with LastMoveTracker with Board2dArray {

  require(k <= m || k <= n)
  require(numPlayers >= 2)

  protected var freePositions: Int = m * n

  protected val k1: Short = (k - 1).toShort

  final val minWinDepth: Int = numPlayers * k1 + 1 //(numPlayers * k) - (numPlayers-1) // np*(k - 1)+1

  protected var _lastPlayer: Byte = numPlayers

  def lastPlayer: Byte = this._lastPlayer

  def playMove(position: Position, player: Byte): Boolean = {
    require(player >= 1 && player <= numPlayers)
    if (board(position) > 0) false
    else {
      board_=(position)(player)
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

  override def gameEnded(): Boolean = gameEnded(depth)

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

  def nextPlayer(): Byte = opponent(lastPlayer)
}
