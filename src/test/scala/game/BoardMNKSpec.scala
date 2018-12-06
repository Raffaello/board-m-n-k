package game

import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardMNKSpec extends WordSpec with Matchers {
  
  "5x5x3 Game" should {
    val m: Short = 5
    val n: Short = 5
    val k: Short = 3
    "in progress" in {
      val game = new BoardMNK(m, n, k)

      game.ended() should be(false)
    }

    for (p <- NumericRange.inclusive[Byte](1, 2, 1)) {
      "won by player " + p.toString must {
        var score = 0
        if (p == 1) {
          score = 1
        } else {
          score = -1
        }
        "by rows player" in {
          
          for (i <- 0 until m) {
            for (j <- 0 to n-k) {
              val game = new BoardMNK(m, n, k)
              game.board.update(i, Array.tabulate(n)(x => if(x >= j && x< k + j) p else 0))
              game.ended() should be(true)
              game.score() should be(score)
            }
          }
        }

        "by cols" in {
          for (j <- 0 until n) {
            for (i <- 0 to m-k) {
              val game = new BoardMNK(m, n, k)
              for(kk <- 0 until k) {
                game.board(i+kk)(j) = p
              }

              game.ended() should be(true)
              game.score() should be(score)
            }
          }
        }

        "by Diagonals Top Left -> Bottom Right" in {
          val game = new BoardMNK(m, n, k)
          for (i <- 0 until 3) {
            game.board(i)(i) = p
          }

          game.ended() should be(true)
          game.score() should be(score)
        }

        "by diagonals Bottom Left -> Top Right" in {
          val game = new BoardMNK(m, n, k)
          for (i <- 0 until 3) {
            game.board(2 - i)(i) = p
          }

          game.ended() should be(true)
          game.score() should be(score)
        }
      }
    }
  }
}
