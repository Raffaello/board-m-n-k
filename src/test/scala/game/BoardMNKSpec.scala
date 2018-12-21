package game

import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardMNKSpec extends WordSpec with Matchers {

  "3x3x3 board" must {
    "P1 win" in {
      val game = new BoardMNK(3, 3, 3)
      game.playMove((0,0), 1)
      game.playMove((1,1), 2)
      game.playMove((0,1), 1)
      game.playMove((1,0), 2)
      game.playMove((0,2), 1)

      game.gameEnded(4) shouldEqual true
      game.score() shouldEqual 1
      game.LookUps.won shouldEqual Some(true)
      game.LookUps.lastPlayerIdx shouldEqual 0

      game.LookUps.rows(0)(0) shouldEqual 3
      game.LookUps.rows(1)(0) shouldEqual 0
      game.LookUps.rows(2)(0) shouldEqual 0

      game.LookUps.rows(0)(1) shouldEqual 0
      game.LookUps.rows(1)(1) shouldEqual 2
      game.LookUps.rows(2)(1) shouldEqual 0

      game.LookUps.cols(0)(0) shouldEqual 1
      game.LookUps.cols(1)(0) shouldEqual 1
      game.LookUps.cols(2)(0) shouldEqual 1

      game.LookUps.cols(0)(1) shouldEqual 1
      game.LookUps.cols(1)(1) shouldEqual 1
      game.LookUps.cols(2)(1) shouldEqual 0
    }

    "P2 win" in {
      val game = new BoardMNK(3, 3, 3)
      game.playMove((0,0), 1)
      game.playMove((1,1), 2)
      game.playMove((2,0), 1)
      game.playMove((1,0), 2)
      game.playMove((0,2), 1)
      game.playMove((1,2), 2)

      game.gameEnded(5) shouldEqual true
      game.score() shouldEqual -1
      game.LookUps.won shouldEqual Some(true)
      game.LookUps.lastPlayerIdx shouldEqual 1

      game.LookUps.rows(0)(0) shouldEqual 2
      game.LookUps.rows(1)(0) shouldEqual 0
      game.LookUps.rows(2)(0) shouldEqual 1

      game.LookUps.rows(0)(1) shouldEqual 0
      game.LookUps.rows(1)(1) shouldEqual 3
      game.LookUps.rows(2)(1) shouldEqual 0

      game.LookUps.cols(0)(0) shouldEqual 2
      game.LookUps.cols(1)(0) shouldEqual 0
      game.LookUps.cols(2)(0) shouldEqual 1

      game.LookUps.cols(0)(1) shouldEqual 1
      game.LookUps.cols(1)(1) shouldEqual 1
      game.LookUps.cols(2)(1) shouldEqual 1
    }

    "STALE" in {
      val game = new BoardMNK(3, 3, 3)
      game.playMove((0,0), 1)
      game.playMove((0,1), 2)
      game.playMove((0,2), 1)
      game.playMove((1,1), 2)
      game.playMove((1,0), 1)
      game.playMove((2,2), 2)
      game.playMove((1,2), 1)
      game.playMove((2,0), 2)
      game.playMove((2,1), 1)

      game.gameEnded(8) shouldEqual true
      game.score() shouldEqual 0
      game.LookUps.won shouldEqual Some(false)
      game.LookUps.lastPlayerIdx shouldEqual 0

      game.LookUps.rows(0)(0) shouldEqual 2
      game.LookUps.rows(1)(0) shouldEqual 2
      game.LookUps.rows(2)(0) shouldEqual 1

      game.LookUps.rows(0)(1) shouldEqual 1
      game.LookUps.rows(1)(1) shouldEqual 1
      game.LookUps.rows(2)(1) shouldEqual 2

      game.LookUps.cols(0)(0) shouldEqual 2
      game.LookUps.cols(1)(0) shouldEqual 1
      game.LookUps.cols(2)(0) shouldEqual 2

      game.LookUps.cols(0)(1) shouldEqual 1
      game.LookUps.cols(1)(1) shouldEqual 2
      game.LookUps.cols(2)(1) shouldEqual 1
    }

    "P1 win diagonalTL" in {
      val game = new BoardMNK(3, 3, 3)
      game.playMove((0,0), 1)
      game.playMove((0,1), 2)
      game.playMove((1,1), 1)
      game.playMove((1,0), 2)
      game.playMove((2,2), 1)

      game.gameEnded(4) shouldEqual true
      game.score() shouldEqual 1
      game.LookUps.won shouldEqual Some(true)
      game.LookUps.lastPlayerIdx shouldEqual 0

      game.LookUps.rows(0)(0) shouldEqual 1
      game.LookUps.rows(1)(0) shouldEqual 1
      game.LookUps.rows(2)(0) shouldEqual 1

      game.LookUps.rows(0)(1) shouldEqual 1
      game.LookUps.rows(1)(1) shouldEqual 1
      game.LookUps.rows(2)(1) shouldEqual 0

      game.LookUps.cols(0)(0) shouldEqual 1
      game.LookUps.cols(1)(0) shouldEqual 1
      game.LookUps.cols(2)(0) shouldEqual 1

      game.LookUps.cols(0)(1) shouldEqual 1
      game.LookUps.cols(1)(1) shouldEqual 1
      game.LookUps.cols(2)(1) shouldEqual 0

      // diag is when rows(x)(y) == cols(x)(y) && > 0
    }

    "P1 win diagonalBR" in {
      val game = new BoardMNK(3, 3, 3)
      game.playMove((2,0), 1)
      game.playMove((0,1), 2)
      game.playMove((1,1), 1)
      game.playMove((1,0), 2)
      game.playMove((0,2), 1)

      game.gameEnded(4) shouldEqual true
      game.score() shouldEqual 1
      game.LookUps.won shouldEqual Some(true)
      game.LookUps.lastPlayerIdx shouldEqual 0

      game.LookUps.rows(0)(0) shouldEqual 1
      game.LookUps.rows(1)(0) shouldEqual 1
      game.LookUps.rows(2)(0) shouldEqual 1

      game.LookUps.rows(0)(1) shouldEqual 1
      game.LookUps.rows(1)(1) shouldEqual 1
      game.LookUps.rows(2)(1) shouldEqual 0

      game.LookUps.cols(0)(0) shouldEqual 1
      game.LookUps.cols(1)(0) shouldEqual 1
      game.LookUps.cols(2)(0) shouldEqual 1

      game.LookUps.cols(0)(1) shouldEqual 1
      game.LookUps.cols(1)(1) shouldEqual 1
      game.LookUps.cols(2)(1) shouldEqual 0

      // diag is when rows(x)(y) == cols(x)(y) && > 0
    }

  }
  for {
    m <- NumericRange.inclusive[Short](3, 5, 1)
    n <- NumericRange.inclusive[Short](3, 5, 1)
    k <- NumericRange.inclusive[Short](3, 5, 1)
    if k <= Math.min(m, n)
  } {
    s"${m}X${n}X$k Game" must {
      "in progress" in {
        val game = new BoardMNK(m, n, k)

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
                    val game = new BoardMNK(m, n, k)
                    for (x <- j until k + j) game.playMove((i.toShort, x.toShort), p)
                    game.gameEnded(game.minWinDepth) should be(true)
                    game.score() should be(score)
                  }

                  s"col $j reverse" in {
                    val game = new BoardMNK(m, n, k)
                    for (x <- k - 1 + j to j by -1) game.playMove((i.toShort, x.toShort), p)
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
                    val game = new BoardMNK(m, n, k)
                    for (kk <- 0 until k) game.playMove(((i + kk).toShort, j.toShort), p)
                    game.gameEnded(game.minWinDepth) should be(true)
                    game.score() should be(score)
                  }

                  s"row $i reverse" in {
                    val game = new BoardMNK(m, n, k)
                    for (kk <- k - 1 to 0 by - 1) game.playMove(((i + kk).toShort, j.toShort), p)
                    game.gameEnded(game.minWinDepth) should be(true)
                    game.score() should be(score)
                  }
                }
              }
            }
          }

          "by Diagonals Top Left -> Bottom Right" in {
            val game = new BoardMNK(m, n, k)
            for (i <- 0 until k) game.playMove((i.toShort, i.toShort), p)
            game.gameEnded(game.minWinDepth) should be(true)
            game.score() should be(score)
          }

          "by diagonals Bottom Left -> Top Right" in {
            val game = new BoardMNK(m, n, k)
            for (i <- 0 until k) game.playMove(((k - 1 - i).toShort,i.toShort), p)
            game.gameEnded(game.minWinDepth) should be(true)
            game.score() should be(score)
          }
        }
      }
    }
  }
}
