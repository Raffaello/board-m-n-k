package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Score}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaTranspositionSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta with Transposition" should {
    val game = new BoardTicTacToe2() with AlphaBetaTransposition
    "solve the game" in {
      val t = game.solve()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
      t.depth shouldEqual 0
    }
    "have first move" in {
      val t = game.nextMove()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
      t.depth shouldEqual 0
    }
  }

  "BoardTicTacToe Alpha Beta with Transposition" should {
    val game = new BoardTicTacToe with AlphaBetaTransposition
    "solve the game" in {
      val t = game.solve()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
    }
    "have first move" in {
      val t = game.nextMove()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
      t.depth shouldEqual 0
    }
  }
}
