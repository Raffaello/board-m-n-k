package ai.old

import ai.AiTicTacToeExpectedStats
import game.{BoardTicTacToe2, Status}
import org.scalatest.{FlatSpec, Matchers}

//noinspection NameBooleanParameters
class PackageSpec extends FlatSpec with Matchers {
  "TicTacToe2 game with minimax" should "solve the game" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()

    minimax(game, isMaximizingPlayer = true) shouldEqual 0
    expMiniMax()
  }

  "TicTacToe2 negamax" should "solve the game" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2()
    negamax(game, 1) shouldEqual 0
    expNegamax()
  }

  it should "have expected next move" in {
    val game = new BoardTicTacToe2()
    val s: Status = (0, (0, 0))
    negamaxNextMove(game, 1) shouldEqual s
  }

  "TicTacToe2 Alpha-Beta with Memory" should "solve the game" in new AiTicTacToeExpectedStats {
    val game = new BoardTicTacToe2() with TranspositionTable with WithGetBoard
    alphaBetaWithMem(game, game) shouldEqual Transposition(0.0, 0, 0.0, Double.MaxValue, isMaximizing = true)
    expAlphaBetaWithMemStats(game.transpositions.size)
  }

  "Player 1 TicTacToe2" should "win" in {
    val game = new BoardTicTacToe2() with WithGetBoard
    val status = new TranspositionTable {}
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)

    game.depth shouldBe 6
    negamax(game, 1) shouldBe 1
    minimax(game, isMaximizingPlayer = true) shouldEqual 1
    alphaBetaWithMem(status, game).score should be >= 1.0
  }

  "Player 2 TicTacToe2" should "win" in {
    val game = new BoardTicTacToe2()
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 1)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    game.playMove((1, 2), 2)

    game.depth shouldEqual 7
    //    negamax(game, -1) should be (-1) // cannot return -1 from the first step.
    minimax(game, isMaximizingPlayer = false) should be(-1)
  }
}
