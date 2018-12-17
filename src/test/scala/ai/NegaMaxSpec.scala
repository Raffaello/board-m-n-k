package ai

import game.BoardTicTacToe
import org.scalatest.{FlatSpec, Matchers, WordSpec}

class NegaMaxSpec extends WordSpec with Matchers {

  "Tic Tac Toe negamax" should {
    val game = new BoardTicTacToe() with NegaMax
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(1, 0) shouldEqual(0, (0, 0))
    }
  }
}
