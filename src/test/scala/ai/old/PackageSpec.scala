package ai.old

import game.BoardTicTacToe
import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers {
  "Tic Tac Toe game with minimax" should "solve the game" in {
    val game = new BoardTicTacToe()

    minimax(game, true) shouldEqual 0
  }

  "Tic Tac Toe negamax" should "solve the game" in {
    val game = new BoardTicTacToe()
    negamax(game, 1) shouldEqual 0
    negamaxNextMove(game, 1) shouldEqual(0, 0, 0)
  }

  "Tic Tac Toe Alpha-Beta with Memory" should "solve the game" in {
    val game = new BoardTicTacToe() with TranspositionTable with withGetBoard
    alphaBetaWithMem(game, game) shouldEqual Transposition(0.0, 0, 0.0, Double.MaxValue, true)
  }

  "Player 1 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe() with withGetBoard
    val status = new TranspositionTable {}
    game.playMove((0, 0), 1)
    game.playMove((0, 1), 1)
    game.playMove((1, 0), 2)
    game.playMove((1, 1), 1)
    game.playMove((2, 1), 2)
    game.playMove((2, 2), 2)
    negamax(game, 1) should be(1)
    minimax(game, true) shouldEqual 1
    alphaBetaWithMem(status, game).score should be >= 1.0
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
    //    negamax(game, -1) should be (-1) // cannot return -1 from the first step.
    minimax(game, false) should be(-1)
  }
}
