package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Score, Status}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta" should {
    val game = new BoardTicTacToe2() with AlphaBeta
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      val abStatus: ABStatus[Score] = ((0, Int.MaxValue), (0, (0, 0)))
      game.nextMove() shouldEqual abStatus
    }

    "have 2nd move" in {
      val s: Status = (0, (1, 1))
      game.playMove((0, 0), 1)
      game.depth shouldBe 1
      val (_, status) = game.nextMove(maximizing = false, game.depth)
      status shouldEqual s
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
      game.nextMove() shouldEqual abStauts
    }

    "draw the game" in {
      val game = new BoardTicTacToe with AlphaBeta

      game.solve shouldBe 0
    }
  }
}
