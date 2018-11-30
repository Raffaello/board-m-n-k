package ai

import org.scalatest.{FlatSpec, Matchers}
import game.BoardTicTacToe

class PackageSpec extends FlatSpec with Matchers {


  "Tic Tac Toe game with minimax" should "be generate all states and win" in {
    val game = new BoardTicTacToe()
    minimax(game, true) should be(0)
    println (s"total calls: $minimaxCounter")
  }

  "Tic Tac Toe game with minimax depth" should "be generate all states and win" in {
    val game = new BoardTicTacToe()
    minimaxCounter=0
    minimax(game, 0, true) should be(0)
    println (s"total calls: $minimaxCounter")
  }

  "Tic Tac Toe negamax" should "solve the game" in {
    val game = new BoardTicTacToe()
    negamax(game, 1) should be(0)
//    println (s"total calls: $negamaxCounter")
  }

  "Tic Tac Toe alphaBeta" should "solve the game" in {
    val game = new BoardTicTacToe()
    alphaBeta(game, maximizingPlayer = true) should be(0)
//    println (s"total calls: $alphaBetaCounter")
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
  }

  "Player 2 Tic Tac Toe" should "win" in {
    val game = new BoardTicTacToe()
    game.board(0)(0) = 1
    game.board(0)(1) = 1
    game.board(1)(0) = 2
    game.board(1)(1) = 1
    game.board(2)(1) = 2
    game.board(2)(2) = 2
    game.board(2)(1) = 1
    negamax(game, -1) should be (-1)
  }
}
