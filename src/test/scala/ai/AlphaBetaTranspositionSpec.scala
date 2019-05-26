package ai

import game.{BoardTicTacToe, BoardTicTacToe2, Position, Score, Status}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaTranspositionSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta with Transposition" should {
    "solve the game" in {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val t = game.solve()
      t.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      t.ab shouldEqual ab
      t.depth shouldEqual 0
    }
    "have first move" in {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val (score, pos): Status = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      (a, b) shouldEqual ab
      game.depth shouldEqual 0
      val p: Position = (0, 0)
      pos shouldBe p

    }

    "have 2nd move" in {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val s: Status = (0, (1, 1))
      game.playMove((0, 0), 1)
      val status: Status = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      status shouldEqual s
      val ab: AB[Score] = (Int.MinValue, 0)
      (a, b) shouldBe ab
    }
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
      val status: Status = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      val s: Status = (0, (0, 0))
      status shouldEqual s
      val ab: AB[Score] = (0, Int.MaxValue)
      (a, b) shouldEqual ab
      game.depth shouldEqual 0
    }
  }
}
