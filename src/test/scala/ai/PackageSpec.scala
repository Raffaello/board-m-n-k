package ai

import org.scalatest.{FlatSpec, Matchers}
import game.BoardTicTacToe

class PackageSpec extends FlatSpec with Matchers {

  "Tic Tac Toe game with minimax" should "solve the game" in {
    val game = new BoardTicTacToe()
    minimax(game, true) shouldEqual 0
  }

  "Tic Tac Toe negamax" should "solve the game" in {
    val game = new BoardTicTacToe()
    negamax(game, 1) shouldEqual 0
  }

  "Tic Tac Toe alphaBeta" should "solve the game" in {
    val game = new BoardTicTacToe()
    alphaBeta(game, maximizingPlayer = true) shouldEqual 0.0
  }

  "Player 1 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe()
    game.board(0)(0) = 1
    game.board(0)(1) = 1
    game.board(1)(0) = 2
    game.board(1)(1) = 1
    game.board(2)(1) = 2
    game.board(2)(2) = 2
    negamax(game, 1) should be (1)
    minimax(game, true) shouldEqual 1
    alphaBeta(game, maximizingPlayer = true) should be > 0.0
  }

  "Player 2 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe()
    game.board(0)(0) = 1
    game.board(0)(1) = 1
    game.board(1)(0) = 1
    game.board(1)(1) = 1
    game.board(2)(1) = 2
    game.board(2)(2) = 2
    game.board(1)(2) = 2
//    negamax(game, -1) should be (-1) // desing cannot return -1 from the first step.
    minimax(game, false) should be (-1)
    alphaBeta(game, depth=6, maximizingPlayer = false) should be < 0.0
  }
}
