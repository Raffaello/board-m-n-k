package ai

import game.types.{Position, Status}
import game.{BoardTicTacToe, BoardTicTacToe2, Score}
import org.scalatest.{Matchers, WordSpec}

class NegaMaxSpec extends WordSpec with Matchers {

  "TicTacToe2 negamax" should {
    val game = new BoardTicTacToe2() with NegaMax
    "solve the game" in new AiTicTacToeExpectedStats {
      game.solve shouldEqual 0
      expNegamax(game.Stats.totalCalls)
    }

    "have first move" in {
      val s: Status[Score] = Status(0, Position(0, 0))
      game.nextMove shouldEqual s
    }

    "have 2nd move" in {
      val s: Status[Score] = Status(0, Position(1, 1))
      game.playMove(Position(0, 0), 1)
      game.depth shouldBe 1
      game.nextMove shouldEqual s
    }
  }

  "BoardTicTacToe negamax" should {
    "solve the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe with NegaMax
      game.solve shouldEqual 0
      expNegamax(game.Stats.totalCalls)
    }

    "have first move" in {
      val game = new BoardTicTacToe with NegaMax
      val s: Status[Score] = Status(0, Position(0, 0))
      game.nextMove shouldEqual s
    }
  }
}
