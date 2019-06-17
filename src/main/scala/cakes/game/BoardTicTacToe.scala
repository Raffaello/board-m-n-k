package cakes.game

import cakes.game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import cakes.game.types.{BOARD_1D_ARRAY, BOARD_2D_ARRAY, BOARD_BIT_BOARD, BoardMNTypeEnum}

abstract class BoardTicTacToe extends BoardMNK(3, 3, 3)

object BoardTicTacToe {
  def apply(boardType: BoardMNTypeEnum): BoardTicTacToe = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardTicTacToe with Board1dArray
      case BOARD_2D_ARRAY => new BoardTicTacToe with Board2dArray
      // Working with subClassing.
      case BOARD_BIT_BOARD => new BitBoardTicTacToe with BoardBitBoard
    }
  }
}
