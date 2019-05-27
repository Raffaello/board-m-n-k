package game

import ai.AiTicTacToeExpectedStats
import ai.old.Drafts
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BitBoardTicTacToeSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  "A Game" should {
    "in progress" in {
      val game = new BitBoardTicTacToe()

      game.gameEnded(0) shouldBe false
    }

    "playMove/undoMove" should {
      "p1" in {
        val p: Player = 1.toByte
        val game = new BitBoardTicTacToe
        game.playMove(Position(0, 0), p) shouldBe true
        game._board shouldBe 1
        game.undoMove(Position(0, 0), p) shouldBe true
        game._board shouldBe 0
      }

      "p2" in {
        val p: Player = 2.toByte
        val game = new BitBoardTicTacToe
        game.playMove(Position(0, 0), p) shouldBe true
        game._board shouldBe 1 << 9
        game.undoMove(Position(0, 0), p) shouldBe true
        game._board shouldBe 0
      }

      "be valid always" in {
        val ps = for (i <- Gen.choose[Byte](1, 2)) yield i
        val ms = for (i <- Gen.choose[Short](0, 2)) yield i
        val ns = for (i <- Gen.choose[Short](0, 2)) yield i
        val game = new BitBoardTicTacToe

        forAll(ps, ns, ms) { (p: Player, n: Short, m: Short) =>
          val pos: Position = Position(m, n)
          game.playMove(pos, p) shouldBe true

          val pBoard: BitBoard = game._board >> 9 * (p - 1)
          // (0,0) 0 (0,1) 1 (0,2) 2
          // (1,0) 3       4       5
          // (2,0) 6 (2,1) 7 (2,2) 8
          val bit = 1 << (m * 3 + n)

          pBoard shouldBe bit
          game._board shouldBe (bit << 9 * (p - 1))

          game.undoMove(pos, p) shouldBe true
          game._board shouldBe 0
        }
      }
    }

    for (p <- 1 to 2) {
      s"checkWins p$p" should {
        val s = if (p == 1) 1 else -1
        for (i <- 0 until 3) {
          s"be correct with row $i" in {
            val game = new BitBoardTicTacToe
            for (j <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p.toByte) shouldBe true
            game.gameEnded() shouldBe true
            game.score() shouldBe s
          }

          s"be correct with col $i" in {
            val game = new BitBoardTicTacToe
            for (j <- 0 until 3) game.playMove(Position(j.toShort, i.toShort), p.toByte) shouldBe true
            game.gameEnded() shouldBe true
            game.score() shouldBe s
          }
        }

        s"be correct with diag SE" in {
          val game = new BitBoardTicTacToe
          for (j <- 0 until 3) game.playMove(Position(j.toShort, j.toShort), p.toByte) shouldBe true
          game.gameEnded() shouldBe true
          game.score() shouldBe s
        }

        s"be correct with diag NE" in {
          val game = new BitBoardTicTacToe
          for (j <- 0 until 3) game.playMove(Position(j.toShort, (2-j).toShort), p.toByte) shouldBe true
          game.gameEnded() shouldBe true
          game.score() shouldBe s
        }
      }
    }

    "not ended" in {
      val game = new BitBoardTicTacToe
      game.playMove(Position(0,0), 1) shouldBe true
      game.playMove(Position(0,1), 2) shouldBe true
      game.playMove(Position(0,2), 1) shouldBe true
      game.playMove(Position(1,0), 2) shouldBe true
      game.gameEnded() shouldBe false
      game.depth shouldEqual 4
      game.score() shouldBe 0
    }

    "draw (drafts)" in new AiTicTacToeExpectedStats {
      val game = new BitBoardTicTacToe
      Drafts.alphaBetaBit(game) shouldBe 0.0
      expAlphaBeta()
    }

    for (p <- NumericRange.inclusive[Byte](1, 2, 1)) {
      "won by player " + p.toString must {
        var score = 0
        if (p === 1) {
          score = 1
        } else {
          score = -1
        }
        "by rows player" in {

          for (i <- 0 until 3) {
            val game = new BitBoardTicTacToe()
            for (j <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        "by cols" in {
          for (j <- 0 until 3) {
            val game = new BitBoardTicTacToe()
            for (i <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
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
