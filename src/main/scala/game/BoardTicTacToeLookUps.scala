package game

import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.types.{BOARD_1D_ARRAY, BOARD_2D_ARRAY, BOARD_BIT_BOARD, BoardMNTypeEnum}

// TODO potentailly BoardTicTacToe should just include the trait lookups without need this one.
abstract class BoardTicTacToeLookUps extends BoardMNKLookUp(3, 3, 3)

object BoardTicTacToeLookUps {
  def apply(boardType: BoardMNTypeEnum): BoardTicTacToeLookUps = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardTicTacToeLookUps with Board1dArray
      case BOARD_2D_ARRAY => new BoardTicTacToeLookUps with Board2dArray
      // Working with subClassing.
      case BOARD_BIT_BOARD => new BoardTicTacToeLookUps with BoardBitBoard
    }
  }
}
