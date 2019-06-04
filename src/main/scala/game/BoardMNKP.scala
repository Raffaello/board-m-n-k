package game

import cats.implicits._
import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.boards.{BoardDepthAware, BoardMN, BoardPlayers, LastMoveTracker}
import game.types._

abstract class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n)
  with BoardDepthAware with LastMoveTracker with BoardPlayers {

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
  override def score(): Int = score(lastPlayer)

  /**
    * if player won 1
    * if player lost -1
    * if it is a draw 0
    */
  def score(player: Player): Int = ???

  // check if board has k in a row of any player then return true otherwise false
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

object BoardMNKP {
  def apply(m: Short, n: Short, k: Short, numPlayers: Player, boardType: BoardMNTypeEnum): BoardMNKP = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardMNKP(m, n, k, numPlayers) with Board1dArray
      case BOARD_2D_ARRAY => new BoardMNKP(m, n, k, numPlayers) with Board2dArray
      case BOARD_BIT_BOARD => new BoardMNKP(m, n, k, numPlayers) with BoardBitBoard
    }
  }
}
