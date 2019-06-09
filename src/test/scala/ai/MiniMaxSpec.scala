package ai

import game.boards.implementations.Board2dArray
import game.{BoardMNKLookUp, BoardTicTacToe, BoardTicTacToe2}

class MiniMaxSpec extends TicTacToeSpec {

  sealed class BoardTicTacToe2MiniMax extends BoardTicTacToe2 with MiniMax

  sealed class BoardTicTacToeMiniMax extends BoardTicTacToe with MiniMax with Board2dArray

  aiBoard(new BoardTicTacToe2MiniMax)
  aiBoard(new BoardTicTacToeMiniMax)

  "BoardMNKLookup(3, 3, 3)" should {
    "draw" in {
      val game = new BoardMNKLookUp(3, 3, 3) with Board2dArray with MiniMax
      game.solve shouldBe 0
    }
  }
}
