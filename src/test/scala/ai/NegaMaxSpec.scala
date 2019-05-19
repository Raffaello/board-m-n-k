package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Status}
import org.scalatest.{Matchers, WordSpec}

class NegaMaxSpec extends WordSpec with Matchers {

  "TicTacToe2 negamax" should {
    val game = new BoardTicTacToe2() with NegaMax
    "solve the game" in {
      game.solve shouldEqual 0
    }

    "have first move" in {
      val s: Status = (0, (0, 0))
      game.nextMove(1, 0) shouldEqual s
    }

    "have 2nd move" in {
      val s: Status = (0, (1, 1))
      game.playMove((0, 0), 1)
      game.nextMove(-1, 0) shouldEqual s
    }
  }

  "BoardTicTacToe negamax" should {
    "solve the game" in {
      val game = new BoardTicTacToe with NegaMax
      game.solve shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with NegaMax
      val s: Status = (0, (0, 0))
      game.nextMove(1, 0) shouldEqual s
    }
  }
}
