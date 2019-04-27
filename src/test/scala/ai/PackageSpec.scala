package ai

import game.BoardTicTacToe
import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers {
  "Tic Tac Toe alphaBeta" should "solve the game" in {
    val game = new BoardTicTacToe()
    alphaBeta(game, maximizingPlayer = true) shouldEqual 0.0
    alphaBetaNextMove(game, 0, Double.MinValue, Double.MaxValue, true) shouldEqual(0.0, 0, 0, 0.0, Double.MaxValue)
  }

  "Player 1 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe()
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    alphaBeta(game, maximizingPlayer = true) should be >= 1.0
  }

  "Player 2 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe()
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
