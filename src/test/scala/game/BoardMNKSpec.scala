package game

import game.types.{BOARD_1D_ARRAY, BOARD_2D_ARRAY, BOARD_BIT_BOARD, Position}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardMNKSpec extends WordSpec with Matchers {

  "3x3x3 board (alias BoardTicTacToe)" must {
    "P1 win" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(1, 1), 2) shouldBe true
      game.playMove(Position(0, 1), 1) shouldBe true
      game.playMove(Position(1, 0), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true

      game.depth shouldBe 5
      game.gameEnded() shouldEqual true
      game.score() shouldEqual 1

      game.lookUps.ended shouldEqual Some(true)
      game.lookUps.lastPlayerIdx shouldEqual 0
      game.lookUps.rows shouldBe Array(Array(3, 0), Array(0, 2), Array(0, 0))
      game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
    }

    "P2 win" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(1, 1), 2) shouldBe true
      game.playMove(Position(2, 0), 1) shouldBe true
      game.playMove(Position(1, 0), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true
      game.playMove(Position(1, 2), 2) shouldBe true

      game.depth shouldBe 6
      game.gameEnded() shouldEqual true
      game.score() shouldEqual -1
      game.lookUps.ended shouldEqual Some(true)
      game.lookUps.lastPlayerIdx shouldEqual 1
      game.lookUps.rows shouldBe Array(Array(2, 0), Array(0, 3), Array(1, 0))
      game.lookUps.cols shouldBe Array(Array(2, 1), Array(0, 1), Array(1, 1))
    }

    "STALE" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(0, 1), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true
      game.playMove(Position(1, 1), 2) shouldBe true
      game.playMove(Position(1, 0), 1) shouldBe true
      game.playMove(Position(2, 2), 2) shouldBe true
      game.playMove(Position(1, 2), 1) shouldBe true
      game.playMove(Position(2, 0), 2) shouldBe true
      game.playMove(Position(2, 1), 1) shouldBe true

      game.depth shouldBe 9
      game.gameEnded() shouldEqual true
      game.score() shouldEqual 0
      game.lookUps.ended shouldEqual Some(false)
      game.lookUps.lastPlayerIdx shouldEqual 0
      game.lookUps.rows shouldBe Array(Array(2, 1), Array(2, 1), Array(1, 2))
      game.lookUps.cols shouldBe Array(Array(2, 1), Array(1, 2), Array(2, 1))
    }

    "P1 win diagonalTL" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(0, 1), 2) shouldBe true
      game.playMove(Position(1, 1), 1) shouldBe true
      game.playMove(Position(1, 0), 2) shouldBe true
      game.playMove(Position(2, 2), 1) shouldBe true

      game.depth shouldBe 5
      game.gameEnded() shouldEqual true
      game.score() shouldEqual 1
      game.lookUps.ended shouldEqual Some(true)
      game.lookUps.lastPlayerIdx shouldEqual 0
      game.lookUps.rows shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
      game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
    }

    "P1 win diagonalBR" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.playMove(Position(2, 0), 1) shouldBe true
      game.playMove(Position(0, 1), 2) shouldBe true
      game.playMove(Position(1, 1), 1) shouldBe true
      game.playMove(Position(1, 0), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true

      game.depth shouldBe 5
      game.gameEnded() shouldEqual true
      game.score() shouldEqual 1
      game.lookUps.ended shouldEqual Some(true)
      game.lookUps.lastPlayerIdx shouldEqual 0

      game.lookUps.rows shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
      game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
    }

    "display" in {
      val game = BoardTicTacToe(BOARD_2D_ARRAY)
      game.display() shouldBe
        """ _ | _ | _
          | _ | _ | _
          | _ | _ | _
          |
          |""".stripMargin
    }
  }

  "4x3x3 board" must {
    "game status 121 221 201 001" in {
      val game = BoardMNK(4, 3, 3, BOARD_2D_ARRAY)
      game.minWinDepth shouldBe 5
      game.playMove(Position(0, 0), 1) shouldBe true
      game.playMove(Position(0, 1), 2) shouldBe true
      game.playMove(Position(0, 2), 1) shouldBe true

      game.playMove(Position(1, 0), 2) shouldBe true
      game.playMove(Position(1, 2), 1) shouldBe true
      game.playMove(Position(1, 1), 2) shouldBe true

      game.playMove(Position(3, 2), 1) shouldBe true

      game.playMove(Position(2, 0), 2) shouldBe true
      game.playMove(Position(2, 2), 1) shouldBe true

      game.gameEnded(8) shouldEqual true
      game.score() shouldEqual 1
      game.lookUps.ended shouldEqual Some(true)
      game.lookUps.lastPlayerIdx shouldEqual 0

      game.lookUps.rows shouldBe Array(Array(2, 1), Array(1, 2), Array(1, 1), Array(1, 0))
      game.lookUps.cols shouldBe Array(Array(1, 2), Array(0, 2), Array(4, 0))
    }
  }

  for {
    m <- NumericRange.inclusive[Short](3, 5, 1)
    n <- NumericRange.inclusive[Short](3, 5, 1)
    k <- NumericRange.inclusive[Short](3, 5, 1)
    b <- Seq(BOARD_2D_ARRAY, BOARD_1D_ARRAY, BOARD_BIT_BOARD)
    if k <= Math.min(m, n)
  } {
    val forcedGameEndCheckedValue = k * 2

    s"${m}X${n}X$k Game $b" must {
      "in progress" in {
        val game = BoardMNK(m, n, k, b)

        game.gameEnded(0) should be(false)
      }

      for (p <- NumericRange.inclusive[Byte](1, 2, 1)) {
        "won by player " + p.toString should {
          var score = 0
          if (p == 1) {
            score = 1
          } else {
            score = -1
          }
          "by rows and" should {
            for (i <- 0 until m) {
              s"row $i and " should {
                for (j <- 0 to n - k) {

                  s"col $j" in {
                    val game = BoardMNK(m, n, k, b)
                    for (x <- j until k + j) game.playMove(Position(i.toShort, x.toShort), p)
                    game.gameEnded(forcedGameEndCheckedValue) should be(true)
                    game.score() should be(score)
                  }

                  s"col $j reverse" in {
                    val game = BoardMNK(m, n, k, b)
                    for (x <- k - 1 + j to j by -1) game.playMove(Position(i.toShort, x.toShort), p)
                    game.gameEnded(forcedGameEndCheckedValue) should be(true)
                    game.score() should be(score)
                  }
                }
              }
            }
          }

          "by cols" should {
            for (j <- 0 until n) {

              s"col $j and" should {
                for (i <- 0 to m - k) {

                  s"row $i" in {
                    val game = BoardMNK(m, n, k, b)
                    for (kk <- 0 until k) game.playMove(Position((i + kk).toShort, j.toShort), p)
                    game.gameEnded(forcedGameEndCheckedValue) should be(true)
                    game.score() should be(score)
                  }

                  s"row $i reverse" in {
                    val game = BoardMNK(m, n, k, b)
                    for (kk <- k - 1 to 0 by -1) game.playMove(Position((i + kk).toShort, j.toShort), p)
                    game.gameEnded(forcedGameEndCheckedValue) should be(true)
                    game.score() should be(score)
                  }
                }
              }
            }
          }

          "by Diagonals Top Left -> Bottom Right" in {
            val game = BoardMNK(m, n, k, b)
            for (i <- 0 until k) game.playMove(Position(i.toShort, i.toShort), p)
            game.gameEnded(forcedGameEndCheckedValue) should be(true)
            game.score() should be(score)
          }

          "by diagonals Bottom Left -> Top Right" in {
            val game = BoardMNK(m, n, k, b)
            for (i <- 0 until k) game.playMove(Position((k - 1 - i).toShort, i.toShort), p)
            game.gameEnded(forcedGameEndCheckedValue) should be(true)
            game.score() should be(score)
          }
        }
      }
    }
  }
}
