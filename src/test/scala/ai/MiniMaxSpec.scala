package ai

import game.BoardTicTacToe
import org.scalatest.{Matchers, WordSpec}

class MiniMaxSpec extends WordSpec with Matchers {

  "Tic Tac Toe game with minimax" should  {
    val game = new BoardTicTacToe() with MiniMax
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(true, 0) shouldEqual(0, (0, 0))
    }
  }
}
