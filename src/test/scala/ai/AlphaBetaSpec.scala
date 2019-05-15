package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Score}
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
  }

  "BoardTicTacToe Alpha Beta" should {
    val game = new BoardTicTacToe with AlphaBeta
    "solve the game" in {
      game.solve() shouldEqual 0
    }

    "have first move" in {
      val abStauts: ABStatus[Score] = ((0, Int.MaxValue), (0, (0, 0)))
      game.nextMove() shouldEqual abStauts
    }
  }
}
