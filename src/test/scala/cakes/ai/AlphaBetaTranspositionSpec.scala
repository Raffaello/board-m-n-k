package cakes.ai

import game.{BoardTicTacToe, BoardTicTacToe2}

class AlphaBetaTranspositionSpec extends TicTacToeSpec {

  sealed class BoardTicTacToe2AlphaBetaTransposition extends BoardTicTacToe2
    with AlphaBetaTransposition with TranspositionTable2dArrayString

  sealed class BoardTicTacToeAlphaBetaTransposition extends BoardTicTacToe
    with AlphaBetaTransposition with TranspositionTable2dArrayString

  "BoardTicTacToe2AlphaBetaTransposition" should {
    solveTheGame(new BoardTicTacToe2AlphaBetaTransposition)
    haveFirstMove(new BoardTicTacToe2AlphaBetaTransposition)
    have2ndMove(new BoardTicTacToe2AlphaBetaTransposition)
  }

  "BoardTicTacToeAlphaBetaTransposition" should {
    solveTheGame(new BoardTicTacToeAlphaBetaTransposition)
    haveFirstMove(new BoardTicTacToeAlphaBetaTransposition)
    have2ndMove(new BoardTicTacToeAlphaBetaTransposition)
  }
}
