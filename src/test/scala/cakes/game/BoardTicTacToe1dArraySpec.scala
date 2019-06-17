package cakes.game

import _root_.types.Position
import cakes.ai.{AiTicTacToeExpectedStats, alphaBeta}
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardTicTacToe1dArraySpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  sealed trait getBoard extends BoardTicTacToe1dArray {
    override def board: Board1d = super.board

    override def boardPlayer(pos: Position): Player = super.boardPlayer(pos)
  }

  "A Game" should {
    "be in progress" in {
      val game = new BoardTicTacToe1dArray()

      game.gameEnded(0) should be(false)
    }

    "be valid always" in {
      val game = new BoardTicTacToe1dArray with getBoard
      val emptyBoard: Board1d = Array.ofDim[Player](game.mn)

      val ps = for (i <- Gen.choose[Byte](1, 2)) yield i
      val ms = for (i <- Gen.choose[Short](0, 2)) yield i
      val ns = for (i <- Gen.choose[Short](0, 2)) yield i

      forAll(ps, ns, ms) { (p: Player, n: Short, m: Short) =>
        game.depth shouldBe 0
        game.board shouldBe emptyBoard

        val pos: Position = Position(m, n)
        game.playMove(pos, p) shouldBe true
        game.depth shouldBe 1

        val board = Array.ofDim[Player](game.mn)
        board(game.m * m + n) = p
        game.board shouldBe board
        game.boardPlayer(pos) shouldBe p

        game.undoMove(pos, p) shouldBe true
        game.board shouldBe emptyBoard
        game.boardPlayer(pos) shouldBe 0
      }
    }
    "be solved" in new AiTicTacToeExpectedStats {
      val game = new BoardTicTacToe1dArray
      alphaBeta(game) shouldBe 0.0
      //      expAlphaBeta()
    }

    for (p <- NumericRange.inclusive[Byte](1, 2, 1)) {
      "won by player " + p.toString must {
        val score = if (p === 1) 1 else -1
        "by rows player" in {
          for (i <- 0 until 3) {
            val game = new BoardTicTacToe1dArray()
            for (j <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        "by cols" in {
          for (j <- 0 until 3) {
            val game = new BoardTicTacToe1dArray()
            for (i <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        "by Diagonals Top Left -> Bottom Right" in {
          val game = new BoardTicTacToe1dArray()
          for (i <- 0 until 3) game.playMove(Position(i.toShort, i.toShort), p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }

        "by diagonals Bottom Left -> Top Right" in {
          val game = new BoardTicTacToe1dArray()
          for (i <- 0 until 3) game.playMove(Position((2 - i).toShort, i.toShort), p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }
      }
    }
  }
}
