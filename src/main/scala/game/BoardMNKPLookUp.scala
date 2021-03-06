package game

import cats.implicits._
import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.boards.lookups.TLookUps
import game.types._

abstract class BoardMNKPLookUp(m: Short, n: Short, k: Short, p: Player) extends BoardMNKP(m, n, k, p) with TLookUps {

  override def playMove(position: Position, player: Player): Boolean = {
    val res = super.playMove(position, player)
    if (res) _lookUps.inc(position, player - 1)
    res
  }

  override def undoMove(position: Position, player: Player): Boolean = {
    val res = super.undoMove(position, player)
    if (res) _lookUps.dec(position, player - 1)
    res
  }

  // Todo review this method and lookUps.ended ???
  // TODO !!!!!!!!!!! FIX, SHOULD BE REMOVED OR USE LOOKUPS !!!!!!!!!!!!!!
  override def gameEnded(depth: Int): Boolean = {
//    lookUps.ended.getOrElse(checkWin())
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }
}

object BoardMNKPLookUp {
  def apply(m: Short, n: Short, k: Short, numPlayers: Player, boardType: BoardMNTypeEnum): BoardMNKPLookUp = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardMNKPLookUp(m, n, k, numPlayers) with Board1dArray
      case BOARD_2D_ARRAY => new BoardMNKPLookUp(m, n, k, numPlayers) with Board2dArray
      case BOARD_BIT_BOARD => new BoardMNKPLookUp(m, n, k, numPlayers) with BoardBitBoard
    }
  }
}