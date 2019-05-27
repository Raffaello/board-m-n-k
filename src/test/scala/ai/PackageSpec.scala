package ai

import game.{BoardTicTacToe2, Position}
import org.scalatest.{FlatSpec, Matchers}


class PackageSpec extends FlatSpec with Matchers {
  "TicTacToe2 alphaBeta" should "returning the following first moves" in {
    val game = new BoardTicTacToe2()
    alphaBeta(game) shouldEqual 0.0
    val p: Position = (0, 0)
    val abm1: old.ABMove = (0.0, p, (0.0, Double.MaxValue))
    val abm2: old.ABMove = (0.0, p, (Double.MinValue, 0.0))

    alphaBetaNextMove(game, 0, Double.MinValue, Double.MaxValue, maximizingPlayer = true) shouldEqual abm1
    alphaBetaNextMove(game, 0, Double.MinValue, Double.MaxValue, maximizingPlayer = false) shouldEqual abm2
  }

  "TicTacToe2 alphaBeta with Memory" should "solve the game" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2() with TranspositionTable
    alphaBetaWithMem(game, game) shouldEqual Transposition(0, 0, (0, Int.MaxValue), isMaximizing = true)

    expAlphaBetaWithMemStats(ai.Stats.totalCalls, ai.Stats.cacheHits, game.transpositions.size)
    game.score() shouldBe 0.0
  }

  "Player 1 TicTacToe2" should "win" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)

    game.depth shouldBe 6
    alphaBeta(game, 6) should be >= 1.0
    ai.Stats.totalCalls shouldBe 5
    ai.Stats.cacheHits shouldBe 0
  }

  "Player 2 TicTacToe2" should "win" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 1)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    game.playMove((1, 2), 2)

    game.depth shouldBe 7
    alphaBeta(game, depth = 7, maximizingPlayer = false) should be < 0.0
    ai.Stats.totalCalls shouldBe 1
    ai.Stats.cacheHits shouldBe 0
  }
}
