package ai

import game.BoardTicTacToe2
import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers {
  "TicTacToe2 alphaBeta" should "solve the game" in {
    val game = new BoardTicTacToe2()
    alphaBeta(game) shouldEqual 0.0
    alphaBetaNextMove(game, 0, Double.MinValue, Double.MaxValue, maximizingPlayer = true) shouldEqual(0.0, 0, 0, 0.0, Double.MaxValue)
    alphaBetaNextMove(game, 0, Double.MinValue, Double.MaxValue, maximizingPlayer = false) shouldEqual(0.0, 0, 0, Double.MinValue, 0.0)
  }

  "TicTacToe2 alphaBeta with Memory" should "solve the game" in {
    val game = new BoardTicTacToe2() with TranspositionTable
    alphaBetaWithMem(game, game) shouldEqual Transposition(0, 0, 0, Int.MaxValue, isMaximizing = true)
  }

  "Player 1 TicTacToe2" should "win" in {
    val game = new BoardTicTacToe2()
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    alphaBeta(game) should be >= 1.0
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
    alphaBeta(game, depth = 6, maximizingPlayer = false) should be < 0.0
  }
}
