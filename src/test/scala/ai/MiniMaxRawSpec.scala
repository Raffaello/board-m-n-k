package ai

import game.{BoardTicTacToe, Score, Status}
import org.scalatest.{Matchers, WordSpec}

class MiniMaxRawSpec extends WordSpec with Matchers {

  "BoardTicTacToe with minimaxRaw" should {
    "have first move" in {
      val game = new BoardTicTacToe with MiniMaxRaw
      val s: Status = (0, (0, 0))
      game.nextMove(maximizing = true) shouldEqual s
    }

    "draw the game" in {
      val game = new BoardTicTacToe with MiniMaxRaw
      val score: Score = game.solve
      score shouldBe 0
    }
  }
}
