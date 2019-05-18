package ai

import game.{BoardTicTacToe, Status}
import org.scalatest.{Matchers, WordSpec}

class MiniMaxRawSpec extends WordSpec with Matchers {

  "BoardTicTacToe with minimaxRaw" should {
    "solve the game" in {
      val game = new BoardTicTacToe with MiniMaxRaw
      game.solve() shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with MiniMaxRaw
      val s: Status = (0, (0, 0))
      game.nextMove(maximizing = true, 0) shouldEqual s
    }

    "draw the game" in {
      val game = new BoardTicTacToe with MiniMaxRaw
      game.solve shouldBe 0
    }
  }
}
