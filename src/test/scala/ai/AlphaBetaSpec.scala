package ai

import game.BoardTicTacToe
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaSpec extends WordSpec with Matchers {

  "Tic Tac Toe Alpha Beta" should {
    val game = new BoardTicTacToe() with AlphaBeta
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(true, 0) shouldEqual((0, Int.MaxValue), (0, (0, 0)))
    }
  }
}
