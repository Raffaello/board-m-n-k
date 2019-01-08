package ai

import game.{BoardMNK, BoardTicTacToe}
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

  "BoardMNK 3x3x3 Alpha Beta with Transposition" should {
    val game = new BoardMNK(3, 3, 3) with AlphaBetaTransposition
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
