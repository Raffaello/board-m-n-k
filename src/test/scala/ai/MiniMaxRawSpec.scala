package ai

import game.boards.implementations.Board2dArray
import game.types.{Position, Status}
import game.{BoardTicTacToe, Score}
import org.scalatest.{Matchers, WordSpec}

class MiniMaxRawSpec extends WordSpec with Matchers {

  "BoardTicTacToe with minimaxRaw" should {
    "have first move" in {
      val game = new BoardTicTacToe with Board2dArray with MiniMaxRaw
      val s: Status[Score] = Status(0, Position(0, 0))
      game.nextMove shouldEqual s
    }

    "draw the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe with Board2dArray with MiniMaxRaw
      val score: Score = game.solve
      score shouldBe 0
      expMiniMax(game.Stats.totalCalls)
    }
  }
}
