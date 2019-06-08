package ai

import game.boards.implementations.Board2dArray
import game.{BoardTicTacToe, BoardTicTacToe2}

class AlphaBetaSpec extends TicTacToeSpec {

  sealed class BoardTicTacToe2AlphaBeta extends BoardTicTacToe2 with AlphaBeta

  sealed class BoardTicTacToeAlphaBeta extends BoardTicTacToe with AlphaBeta with Board2dArray

  aiBoard(new BoardTicTacToe2AlphaBeta)
  aiBoard(new BoardTicTacToeAlphaBeta)
}
