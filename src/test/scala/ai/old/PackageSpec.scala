package ai.old

import game.BoardTicTacToe2
import org.scalatest.{FlatSpec, Matchers}

//noinspection NameBooleanParameters
class PackageSpec extends FlatSpec with Matchers {
  "TicTacToe2 game with minimax" should "solve the game" in {
    val game = new BoardTicTacToe2()

    minimax(game, isMaximizingPlayer = true) shouldEqual 0
  }

  "TicTacToe2 negamax" should "solve the game" in {
    val game = new BoardTicTacToe2()
    negamax(game, 1) shouldEqual 0
    negamaxNextMove(game, 1) shouldEqual(0, 0, 0)
  }

  "TicTacToe2 Alpha-Beta with Memory" should "solve the game" in {
    val game = new BoardTicTacToe2() with TranspositionTable with withGetBoard
    alphaBetaWithMem(game, game) shouldEqual Transposition(0.0, 0, 0.0, Double.MaxValue, isMaximizing = true)
  }

  "Player 1 TicTacToe2" should "win" in {
    val game = new BoardTicTacToe2() with withGetBoard
    val status = new TranspositionTable {}
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    negamax(game, 1) should be(1)
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
    //    negamax(game, -1) should be (-1) // cannot return -1 from the first step.
    minimax(game, isMaximizingPlayer = false) should be(-1)
  }
}
