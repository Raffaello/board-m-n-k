package ai

import game.types.{Position, Status}
import game.{BoardTicTacToe, BoardTicTacToe2, Score}
import org.scalatest.{Matchers, WordSpec}

class AlphaBetaTranspositionSpec extends WordSpec with Matchers {

  "TicTacToe2 Alpha Beta with Transposition" should {
    "solve the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val score: Score = game.solve
      score shouldEqual 0
      game.depth shouldEqual 0
      expAlphaBetaTTTrait(game.Stats.totalCalls, game.Stats.cacheHits, game.transpositions.size)
    }
    "have first move" in {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val status: Status[Score] = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      status.score shouldEqual 0
      val ab: AB[Score] = (0, Int.MaxValue)
      (a, b) shouldEqual ab
      game.depth shouldEqual 0
      val p: Position = Position(0, 0)
      status.position shouldBe p

    }

    "have 2nd move" in {
      val game = new BoardTicTacToe2() with AlphaBetaTransposition
      val s: Status[Score] = Status(0, Position(1, 1))
      game.playMove(Position(0, 0), 1)
      val status: Status[Score] = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      status shouldEqual s
      val ab: AB[Score] = (Int.MinValue, 0)
      (a, b) shouldBe ab
    }
  }

  "BoardTicTacToe Alpha Beta with Transposition" should {
    "draw the game" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe with AlphaBetaTransposition
      game.solve shouldBe 0
      expAlphaBetaTTTrait(game.Stats.totalCalls, game.Stats.cacheHits, game.transpositions.size)
    }

    "solve the game" in {
      val game = new BoardTicTacToe with AlphaBetaTransposition

      val score = game.solve
      score shouldEqual 0
    }

    "have first move" in {
      val game = new BoardTicTacToe with AlphaBetaTransposition
      val status: Status[Score] = game.nextMove
      val (a, b) = game.alphaBetaNextMove
      val s: Status[Score] = Status(0, Position(0, 0))
      status shouldEqual s
      val ab: AB[Score] = (0, Int.MaxValue)
      (a, b) shouldEqual ab
      game.depth shouldEqual 0
    }
  }
}
