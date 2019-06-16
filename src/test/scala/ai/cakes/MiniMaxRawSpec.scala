package ai.cakes

import game.boards.implementations.Board2dArray
import game.{BoardTicTacToe, BoardTicTacToe2}

class MiniMaxRawSpec extends TicTacToeSpec {

  sealed class BoardTicTacToe2MiniMaxRaw extends BoardTicTacToe2 with MiniMaxRaw

  sealed class BoardTicTacToeMiniMaxRaw extends BoardTicTacToe with MiniMaxRaw with Board2dArray

  aiBoard(new BoardTicTacToe2MiniMaxRaw)
  aiBoard(new BoardTicTacToeMiniMaxRaw)
}
