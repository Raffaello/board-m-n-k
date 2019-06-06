package ai

import ai.types.{AlphaBetaStatus, AlphaBetaValues}
import game.BoardTicTacToe2
import game.Implicit.convertToPlayer
import game.types.{Position, Status}
import org.scalatest.{FlatSpec, Matchers}


class PackageSpec extends FlatSpec with Matchers {
  "TicTacToe2 alphaBeta" should "returning the following first moves" in {
    val game = new BoardTicTacToe2()
    alphaBeta(game) shouldEqual 0.0
    val p: Position = Position(0, 0)
    val abs1: AlphaBetaStatus[Double] = AlphaBetaStatus(AlphaBetaValues(0.0, Double.MaxValue), Status(0.0, p))
    val abs2: AlphaBetaStatus[Double] = AlphaBetaStatus(AlphaBetaValues(Double.MinValue, 0.0), Status(0.0, p))

    alphaBetaNextMove(game, 0, AlphaBetaValues.alphaBetaValueDouble, maximizingPlayer = true) shouldEqual abs1
    alphaBetaNextMove(game, 0, AlphaBetaValues.alphaBetaValueDouble, maximizingPlayer = false) shouldEqual abs2
  }

  "TicTacToe2 alphaBeta with Memory" should "solve the game" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2() with TranspositionTable
    alphaBetaWithMem(game, game) shouldEqual Transposition(0, 0, AlphaBetaValues(0, Int.MaxValue), isMaximizing = true)

    //    expAlphaBetaWithMemStats(game.transpositions.size)
    game.score() shouldBe 0.0
  }

  "Player 1 TicTacToe2" should "win" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()
    game.playMove(Position(0, 0), 1)
    game.playMove(Position(0, 1), 1)
    game.playMove(Position(1, 0), 2)
    game.playMove(Position(1, 1), 1)
    game.playMove(Position(2, 1), 2)
    game.playMove(Position(2, 2), 2)

    game.depth shouldBe 6
    alphaBeta(game, game.depth) should be >= 1.0
    //    ai.Stats.totalCalls shouldBe 5
    //    ai.Stats.cacheHits shouldBe 0
  }

  "Player 2 TicTacToe2" should "win" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()
    game.playMove(Position(0, 0), 1)
    game.playMove(Position(0, 1), 1)
    game.playMove(Position(1, 0), 1)
    game.playMove(Position(1, 1), 1)
    game.playMove(Position(2, 1), 2)
    game.playMove(Position(2, 2), 2)
    game.playMove(Position(1, 2), 2)

    game.depth shouldBe 7
    alphaBeta(game, game.depth, maximizingPlayer = false) should be < 0.0
    //    ai.Stats.totalCalls shouldBe 1
    //    ai.Stats.cacheHits shouldBe 0
  }
}
