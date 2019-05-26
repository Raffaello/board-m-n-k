package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Score, Status}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta" should {
    "solve the game" in {
      val game = new BoardTicTacToe2() with AlphaBeta
      game.solve() shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe2() with AlphaBeta
      val abStatus: ABStatus[Score] = ((0, Int.MaxValue), (0, (0, 0)))
      val status = game.nextMove
      (game.alphaBetaNextMove, status) shouldEqual abStatus
    }

    "have 2nd move" in {
      val game = new BoardTicTacToe2() with AlphaBeta
      val s: Status = (0, (1, 1))
      game.playMove((0, 0), 1)
      game.depth shouldBe 1
      game.nextMove shouldEqual s
    }
  }

  "BoardTicTacToe Alpha Beta" should {
    "solve the game" in {
      val game = new BoardTicTacToe with AlphaBeta

      game.solve() shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with AlphaBeta

      val abStauts: ABStatus[Score] = ((0, Int.MaxValue), (0, (0, 0)))
      val status = game.nextMove
      (game.alphaBetaNextMove, status) shouldEqual abStauts
    }

    "draw the game" in {
      val game = new BoardTicTacToe with AlphaBeta

      game.solve shouldBe 0
    }
  }
}
