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

//    "have 2nd move" in {
//      val s: Status = (0, (1, 1))
//      game.playMove((0, 0), 1)
//      val t = game.nextMove(maximizing = false)
//      t. shouldEqual s
//    }
  }

  "BoardTicTacToe Alpha Beta with Transposition" should {
    "draw the game" in {
      val game = new BoardTicTacToe with AlphaBetaTransposition

      game.solve shouldBe 0
    }
    "solve the game" in {
      val game = new BoardTicTacToe with AlphaBetaTransposition

      val t = game.solve()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
    }
    "have first move" in {
      val game = new BoardTicTacToe with AlphaBetaTransposition

      val t = game.nextMove()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
      t.depth shouldEqual 0
    }
  }
}
