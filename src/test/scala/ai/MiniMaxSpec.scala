package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Status}
import org.scalatest.{Matchers, WordSpec}

class MiniMaxSpec extends WordSpec with Matchers {

  "TicTacToe2 game with minimax" should {
    val game = new BoardTicTacToe2() with MiniMax
    "solve the game" in {
      game.solve shouldEqual 0
    }

    "have first move" in {
      val s: Status = (0, (0, 0))
      game.nextMove(maximizing = true, game.depth) shouldEqual s
    }

    "have 2nd move" in {
      val s: Status = (0, (1, 1))
      game.playMove((0, 0), 1)
      game.nextMove(maximizing = false, game.depth) shouldEqual s
    }
  }

  "BoardTicTacToe with minimax" should {
    "draw the game" in {
      val game = new BoardTicTacToe with MiniMax
      game.solve shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with MiniMax
      val s: Status = (0, (0, 0))
      game.nextMove(maximizing = true, 0) shouldEqual s
    }
  }
}
