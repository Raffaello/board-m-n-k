package ai

import game.{BoardMNK, BoardTicTacToe, BoardTicTacToe2}
import org.scalatest.{Matchers, WordSpec}

class NegaMaxSpec extends WordSpec with Matchers {

  "TicTacToe2 negamax" should {
    val game = new BoardTicTacToe2() with NegaMax
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(1, 0) shouldEqual(0, (0, 0))
    }
  }

  "BoardTicTacToe negamax" should {
    val game = new BoardTicTacToe with NegaMax
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      game.nextMove(1, 0) shouldEqual(0, (0, 0))
    }
  }
}
