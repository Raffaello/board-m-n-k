package cakes.game

import cats.implicits._
import cakes.game.Implicit.convertToPlayer
import cakes.game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import cakes.game.boards.{BoardDepthAware, BoardMN, BoardPlayerScore}
import cakes.game.types._

abstract class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Player) extends BoardMN(m, n)
  with BoardDepthAware with BoardPlayerScore {

  require(k <= m || k <= n)
  require(numPlayers >= 2)

  def playMove(position: Position, player: Player): Boolean = {
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

  def undoMove(position: Position, player: Player): Boolean = {
    if (boardPlayer(position) === player) {
      boardPlayer_=(position)(0)
      freePositions += 1
      _depth -= 1
      true
    } else false
  }

  // TODO 1 should be aiPlayer
  def score(): Score = {
    if (checkWin()) _lastPlayer match {
      case 1 => 1
      case _ => -1
    }
    else 0
  }

  override def score(player: Player): Score = checkScore(player)

  override def gameEnded(): Boolean = gameEnded(depth)

  def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }

  def opponent(player: Player): Player = {
    assert(player >= 1 && player <= numPlayers)
    (player % numPlayers) + 1
  }

  def nextPlayer(): Player = opponent(_lastPlayer)
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
