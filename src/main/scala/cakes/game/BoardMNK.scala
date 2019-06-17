package cakes.game

import cakes.game.boards.BoardDisplay2Players
import cakes.game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import cakes.game.types._

/**
  * TODO: potentially split in BoardNMK and BoardMNKLookUp (traits)
  */
abstract class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2)
  with BoardDisplay2Players {

  // todo remove
  override def score(player: Player): Score = score()
}

object BoardMNK {
  def apply(m: Short, n: Short, k: Short, boardType: BoardMNTypeEnum): BoardMNK = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardMNK(m, n, k) with Board1dArray
      case BOARD_2D_ARRAY => new BoardMNK(m, n, k) with Board2dArray
      case BOARD_BIT_BOARD => new BoardMNK(m, n, k) with BoardBitBoard
    }
  }
}
