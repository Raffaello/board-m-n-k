package ai

import game.{BoardTicTacToe, BoardTicTacToe2}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta" should {
    val game = new BoardTicTacToe2() with AlphaBeta
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(true, 0) shouldEqual((0, Int.MaxValue), (0, (0, 0)))
    }
  }

  "BoardTicTacToe Alpha Beta" should {
    val game = new BoardTicTacToe with AlphaBeta
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(true, 0) shouldEqual((0, Int.MaxValue), (0, (0, 0)))
    }
  }
}
