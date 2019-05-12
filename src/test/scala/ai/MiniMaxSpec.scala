package ai

import game.{BoardMNK, BoardTicTacToe, BoardTicTacToe2}
import org.scalatest.{Matchers, WordSpec}

class MiniMaxSpec extends WordSpec with Matchers {

  "TicTacToe2 game with minimax" should {
    val game = new BoardTicTacToe2() with MiniMax
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(true, 0) shouldEqual(0, (0, 0))
    }
  }

  "BoardTicTacToe with minimax" should {
    val game = new BoardTicTacToe with MiniMax
    "solve the game" in {
      val game = new BoardTicTacToe with MiniMax
      game.solve() shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with MiniMax
      game.nextMove(true, 0) shouldEqual(0, (0, 0))
    }
  }
}
