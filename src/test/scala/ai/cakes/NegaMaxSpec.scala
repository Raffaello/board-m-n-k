package ai.cakes

import game.boards.implementations.Board2dArray
import game.{BoardTicTacToe, BoardTicTacToe2}

class NegaMaxSpec extends TicTacToeSpec {

  sealed class BoardTicTacToe2NegaMax extends BoardTicTacToe2 with NegaMax

  sealed class BoardTicTacToeNegaMax extends BoardTicTacToe with NegaMax with Board2dArray

  aiBoard(new BoardTicTacToe2NegaMax)
  aiBoard(new BoardTicTacToeNegaMax)
}
