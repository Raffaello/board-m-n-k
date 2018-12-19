package ai

import game.BoardTicTacToe
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaTranspositionSpec extends WordSpec with Matchers {

  "Tic Tac Toe Alpha Beta with Transposition" should {
    val game = new BoardTicTacToe() with AlphaBetaTransposition
    "solve the game" in {
      val t = game.solve()
      t.score shouldEqual 0
      t.alpha shouldEqual 0
      t.beta shouldEqual Int.MaxValue
      t.depth shouldEqual 0
    }

    "have first move" in {
      val t = game.nextMove(true, 0)
      t.score shouldEqual 0
      t.alpha shouldEqual 0
      t.beta shouldEqual Int.MaxValue
      t.depth shouldEqual 0
    }
  }
}