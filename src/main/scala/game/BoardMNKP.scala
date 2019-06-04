package game

import cats.implicits._
import game.boards.implementations.Board2dArray
import game.boards.{BoardDepthAware, BoardMN, BoardPlayers, LastMoveTracker}
import game.types.Position

/**
  * TODO Board2dArray trait should not be included here... remove it later.
  * TODO Board2dArray has to be a type of boards not a with trait
  *      used for generateMoves.
  *
  * Error:(13, 7) class BoardMNKP needs to be abstract, since:
  * it has 4 unimplemented members.
  * /** As seen from class BoardMNKP, the missing signatures are as follows.
  * *  For convenience, these are usable as stub implementations.
  **/
  * // Members declared in game.types.BoardMNType
  * protected def board: game.BoardMNKP#Board = ???
  * protected def boardPlayer(pos: game.types.Position): game.Player = ???
  * protected def boardPlayer_=(pos: game.types.Position)(p: game.Player): Unit = ???
  *
  * // Members declared in game.boards.BoardMovesGenerator
  * def generateMoves(): IndexedSeq[game.types.Position] = ???
  * class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n)
  */
class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n)
  with BoardDepthAware with LastMoveTracker with BoardPlayers /*with Board2dArray*/ {

  require(k <= m || k <= n)
  require(numPlayers >= 2)

  protected val k1: Short = (k - 1).toShort

  final val minWinDepth: Int = numPlayers * k1 + 1 //(numPlayers * k) - (numPlayers-1) // np*(k - 1)+1

  def playMove(position: Position, player: Byte): Boolean = {
    assert(player >= 1 && player <= numPlayers)
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
  override protected def checkWin(): Boolean = ???

  override def gameEnded(): Boolean = gameEnded(depth)

  def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }

  def opponent(player: Byte): Byte = {
    assert(player >= 1 && player <= numPlayers)
    ((player % numPlayers) + 1).toByte
  }

  def nextPlayer(): Byte = opponent(lastPlayer)
}
