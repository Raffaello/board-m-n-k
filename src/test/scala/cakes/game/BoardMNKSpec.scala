package cakes.game

import _root_.types.Position
import cakes.game.types.{BOARD_1D_ARRAY, BOARD_2D_ARRAY, BOARD_BIT_BOARD}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardMNKSpec extends WordSpec with Matchers {

  for (b <- Seq(BOARD_2D_ARRAY, BOARD_1D_ARRAY, BOARD_BIT_BOARD)) {
    s"3x3x3 board $b (alias BoardTicTacToe)" must {
      "P1 win" in {
        val game = BoardMNK(3, 3, 3, b)
        game.playMove(Position(0, 0), 1) shouldBe true
        game.playMove(Position(1, 1), 2) shouldBe true
        game.playMove(Position(0, 1), 1) shouldBe true
        game.playMove(Position(1, 0), 2) shouldBe true
        game.playMove(Position(0, 2), 1) shouldBe true

        game.depth shouldBe 5
        game.gameEnded() shouldEqual true
        game.score() shouldEqual 1

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(true)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 0
        //          cakes.game.lookUps.rows shouldBe Array(Array(3, 0), Array(0, 2), Array(0, 0))
        //          cakes.game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
        //        }
      }

      "P2 win" in {
        val game = BoardTicTacToe(b)
        game.playMove(Position(0, 0), 1) shouldBe true
        game.playMove(Position(1, 1), 2) shouldBe true
        game.playMove(Position(2, 0), 1) shouldBe true
        game.playMove(Position(1, 0), 2) shouldBe true
        game.playMove(Position(0, 2), 1) shouldBe true
        game.playMove(Position(1, 2), 2) shouldBe true

        game.depth shouldBe 6
        game.gameEnded() shouldEqual true
        game.score() shouldEqual -1

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(true)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 1
        //          cakes.game.lookUps.rows shouldBe Array(Array(2, 0), Array(0, 3), Array(1, 0))
        //          cakes.game.lookUps.cols shouldBe Array(Array(2, 1), Array(0, 1), Array(1, 1))
        //        }
      }

      "STALE" in {
        val game = BoardTicTacToe(b)
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

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(false)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 0
        //          cakes.game.lookUps.rows shouldBe Array(Array(2, 1), Array(2, 1), Array(1, 2))
        //          cakes.game.lookUps.cols shouldBe Array(Array(2, 1), Array(1, 2), Array(2, 1))
        //        }
      }

      "P1 win diagonalTL" in {
        val game = BoardTicTacToe(b)
        game.playMove(Position(0, 0), 1) shouldBe true
        game.playMove(Position(0, 1), 2) shouldBe true
        game.playMove(Position(1, 1), 1) shouldBe true
        game.playMove(Position(1, 0), 2) shouldBe true
        game.playMove(Position(2, 2), 1) shouldBe true

        game.depth shouldBe 5
        game.gameEnded() shouldEqual true
        game.score() shouldEqual 1

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(true)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 0
        //          cakes.game.lookUps.rows shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
        //          cakes.game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
        //        }
      }

      "P1 win diagonalBR" in {
        val game = BoardTicTacToe(b)
        game.playMove(Position(2, 0), 1) shouldBe true
        game.playMove(Position(0, 1), 2) shouldBe true
        game.playMove(Position(1, 1), 1) shouldBe true
        game.playMove(Position(1, 0), 2) shouldBe true
        game.playMove(Position(0, 2), 1) shouldBe true

        game.depth shouldBe 5
        game.gameEnded() shouldEqual true
        game.score() shouldEqual 1

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(true)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 0
        //          cakes.game.lookUps.rows shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
        //          cakes.game.lookUps.cols shouldBe Array(Array(1, 1), Array(1, 1), Array(1, 0))
        //        }
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

    s"4x3x3 board $b" must {
      "cakes.game status 121 221 201 001" in {
        val game = BoardMNK(4, 3, 3, b)
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

        // FIX IT
        //        if (b !== BOARD_BIT_BOARD) {
        //          cakes.game.lookUps.ended shouldEqual Some(true)
        //          cakes.game.lookUps.lastPlayerIdx shouldEqual 0
        //          cakes.game.lookUps.rows shouldBe Array(Array(2, 1), Array(1, 2), Array(1, 1), Array(1, 0))
        //          cakes.game.lookUps.cols shouldBe Array(Array(1, 2), Array(0, 2), Array(4, 0))
        //        }
      }
    }

    for {
      m <- NumericRange.inclusive[Short](3, 5, 1)
      n <- NumericRange.inclusive[Short](3, 5, 1)
      k <- NumericRange.inclusive[Short](3, 5, 1)
      if k <= Math.min(m, n)
    } {
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
                      game.gameEnded(game.minWinDepth) should be(true)
                      game.score() should be(score)
                    }

                    s"col $j reverse" in {
                      val game = BoardMNK(m, n, k, b)
                      for (x <- k - 1 + j to j by -1) game.playMove(Position(i.toShort, x.toShort), p)
                      game.gameEnded(game.minWinDepth) should be(true)
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
                      game.gameEnded(game.minWinDepth) should be(true)
                      game.score() should be(score)
                    }

                    s"row $i reverse" in {
                      val game = BoardMNK(m, n, k, b)
                      for (kk <- k - 1 to 0 by -1) game.playMove(Position((i + kk).toShort, j.toShort), p)
                      game.gameEnded(game.minWinDepth) should be(true)
                      game.score() should be(score)
                    }
                  }
                }
              }
            }

            "by Diagonals Top Left -> Bottom Right" in {
              val game = BoardMNK(m, n, k, b)
              for (i <- 0 until k) game.playMove(Position(i.toShort, i.toShort), p)
              game.gameEnded(game.minWinDepth) should be(true)
              game.score() should be(score)
            }

            "by diagonals Bottom Left -> Top Right" in {
              val game = BoardMNK(m, n, k, b)
              for (i <- 0 until k) game.playMove(Position((k - 1 - i).toShort, i.toShort), p)
              game.gameEnded(game.minWinDepth) should be(true)
              game.score() should be(score)
            }
          }
        }
      }
    }
  }
}