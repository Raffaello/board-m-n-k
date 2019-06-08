package game

import ai.AiTicTacToeExpectedStats
import game.Implicit.convertToPlayer
import game.boards.implementations.BoardBitBoard
import game.types.Position
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BitBoardTicTacToeSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  sealed trait getBoard extends BoardBitBoard {
    override def board: BitBoardPlayers = super.board

    override def boardPlayer(pos: Position): Player = super.boardPlayer(pos)
  }

  "A Game" should {
    "in progress" in {
      val game = new BitBoardTicTacToe()

      game.gameEnded(0) shouldBe false
    }

    "playMove/undoMove" should {
      for (p: Player <- NumericRange[Byte](1, 2, 1)) {
        s"p$p" in {
          val game = new BitBoardTicTacToe with getBoard
          val pos = Position(0, 0)
          game.playMove(pos, p) shouldBe true
          game.boardPlayer(pos) shouldBe p
          game.undoMove(pos, p) shouldBe true
          game.boardPlayer(pos) shouldBe 0
        }
      }

      "be valid always" in {
        val emptyBoard: BitBoardPlayers = Array.ofDim[BitBoard](2)

        val ps = for (i <- Gen.choose[Byte](1, 2)) yield i
        val ms = for (i <- Gen.choose[Short](0, 2)) yield i
        val ns = for (i <- Gen.choose[Short](0, 2)) yield i
        val game = new BitBoardTicTacToe with getBoard

        forAll(ps, ns, ms) { (p: Player, n: Short, m: Short) =>
          game.depth shouldBe 0
          game.board shouldBe emptyBoard

          val pos: Position = Position(m, n)
          game.playMove(pos, p) shouldBe true
          game.depth shouldBe 1

          val bit = 1 << (m * 3 + n)
          game.board(p - 1) shouldBe bit
          game.boardPlayer(pos) shouldBe p

          game.undoMove(pos, p) shouldBe true
          game.board(p - 1) shouldBe 0
          game.boardPlayer(pos) shouldBe 0
        }
      }
    }

    for (p <- 1 to 2) {
      s"checkWins p$p" should {
        val s = if (p === 1) 1 else -1
        for (i <- 0 until 3) {
          s"be correct with row $i" in {
            val game = new BitBoardTicTacToe
            for (j <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p) shouldBe true
            game.gameEnded() shouldBe true
            game.score() shouldBe s
          }

          s"be correct with col $i" in {
            val game = new BitBoardTicTacToe
            for (j <- 0 until 3) game.playMove(Position(j.toShort, i.toShort), p) shouldBe true
            game.gameEnded() shouldBe true
            game.score() shouldBe s
          }
        }

        s"be correct with diag SE" in {
          val game = new BitBoardTicTacToe
          for (j <- 0 until 3) game.playMove(Position(j.toShort, j.toShort), p) shouldBe true
          game.gameEnded() shouldBe true
          game.score() shouldBe s
        }

        s"be correct with diag NE" in {
          val game = new BitBoardTicTacToe
          for (j <- 0 until 3) game.playMove(Position(j.toShort, (2 - j).toShort), p) shouldBe true
          game.gameEnded() shouldBe true
          game.score() shouldBe s
        }
      }
    }

    "not ended" in {
      val game = new BitBoardTicTacToe
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(0, 1), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true
      game.playMove(Position(1, 0), 2) shouldBe true
      game.gameEnded() shouldBe false
      game.depth shouldEqual 4
      game.score() shouldBe 0
    }

    "generate all moves correctly" in {
      val game = new BitBoardTicTacToe
      val i = {
        for {
          i <- 0 until 3
          j <- 0 until 3
        } yield Position(i.toShort, j.toShort)
      }

      game.generateMoves() shouldBe i
    }

    "generate some moves correctly" in {
      val game = new BitBoardTicTacToe
      game.playMove(Position(0,0), game.nextPlayer()) shouldBe true
      game.playMove(Position(1,0), game.nextPlayer()) shouldBe true
      game.playMove(Position(2,0), game.nextPlayer()) shouldBe true

      val i = {
        for {
          i <- 0 until 3
          j <- 1 until 3
        } yield Position(i.toShort, j.toShort)
      }

      game.generateMoves() shouldBe i
    }

    "draw" in new AiTicTacToeExpectedStats {
      val game = new BitBoardTicTacToe
      ai.alphaBeta(game) shouldBe 0.0
      //      expAlphaBeta()
    }

    for (p <- NumericRange.inclusive[Byte](1, 2, 1)) {
      "won by player " + p.toString must {
        val score = if (p === 1) 1 else -1
        "by rows player" in {
          for (i <- 0 until 3) {
            val game = new BitBoardTicTacToe()
            for (j <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        for (j <- 0 until 3) {
          s"by cols $j" in {
            val game = new BitBoardTicTacToe()
            for (i <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() shouldBe true
            game.score() shouldBe score
          }
        }

        "by Diagonals Top Left -> Bottom Right" in {
          val game = new BitBoardTicTacToe()
          for (i <- 0 until 3) game.playMove(Position(i.toShort, i.toShort), p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }

        "by diagonals Bottom Left -> Top Right" in {
          val game = new BitBoardTicTacToe()
          for (i <- 0 until 3) game.playMove(Position((2 - i).toShort, i.toShort), p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }
      }
    }
  }
}
