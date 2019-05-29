package game

import cats.implicits._
import game.boards.implementation.Board2dArray
import game.boards.{BoardDepthAware, BoardMN, BoardPlayers, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO Board2dArray trait should not be included here... remove it later.
  * TODO Board2dArray has to be a type of boards not a with trait
  */
class BoardMNKP(boardMNSize: BoardMNSize, val k: Short, val numPlayers: Byte) extends BoardMN(boardMNSize)
  with BoardDepthAware with LastMoveTracker with BoardPlayers with Board2dArray {

  require(k <= m || k <= n)
  require(numPlayers >= 2)

  protected val k1: Short = (k - 1).toShort

  final val minWinDepth: Int = numPlayers * k1 + 1 //(numPlayers * k) - (numPlayers-1) // np*(k - 1)+1

  def playMove(position: Position, player: Byte): Boolean = {
    require(player >= 1 && player <= numPlayers)
    if (boardPlayer(position) > 0) false
    else {
      boardPlayer_=(position)(player)
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  def undoMove(position: Position, player: Byte): Boolean = {

    if (boardPlayer(position) === player) {
      boardPlayer_=(position)(0)
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

  override def display(): String = ???
}
