package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Position, Score, Status}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta" should {
    "solve the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe2() with AlphaBeta
      game.solve shouldEqual 0
      expAlphaBeta(game.Stats.totalCalls)
    }

    "have first move" in {
      val game = new BoardTicTacToe2() with AlphaBeta
      val abStatus: ABStatus[Score] = ((0, Int.MaxValue), (0, Position(0, 0)))
      val status = game.nextMove
      (game.alphaBetaNextMove, status) shouldEqual abStatus
    }

    "have 2nd move" in {
      val game = new BoardTicTacToe2() with AlphaBeta
      val s: Status = (0, Position(1, 1))
      game.playMove(Position(0, 0), 1)
      game.depth shouldBe 1
      game.nextMove shouldEqual s
    }
  }

  "BoardTicTacToe Alpha Beta" should {
    "solve the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe with AlphaBeta

      game.solve shouldEqual 0
      expAlphaBeta(game.Stats.totalCalls)
    }

    "have first move" in {
      val game = new BoardTicTacToe with AlphaBeta

      val abStauts: ABStatus[Score] = ((0, Int.MaxValue), (0, Position(0, 0)))
      val status = game.nextMove
      (game.alphaBetaNextMove, status) shouldEqual abStauts
    }
  }
}
