package game

import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardMNKSpec extends WordSpec with Matchers {

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
