package game

import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardTicTacToeSpec extends WordSpec with Matchers {

  "A Game" should {
    "in progress" in {
      val game = new BoardTicTacToe()

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

          for (i <- 0 until 3) {
            val game = new BoardTicTacToe()
            game.board.update(i, Array.fill(3)(p))
            game.ended() should be(true)
            game.score() should be(score)
          }
        }

        "by cols" in {
          for (j <- 0 until 3) {
            val game = new BoardTicTacToe()
            for (i <- 0 until 3) {
              game.board(j)(i) = p
            }

            game.ended() should be(true)
            game.score() should be(score)
          }
        }

        "by Diagonals Top Left -> Bottom Right" in {
          val game = new BoardTicTacToe()
          for (i <- 0 until 3) {
            game.board(i)(i) = p
          }

          game.ended() should be(true)
          game.score() should be(score)
        }

        "by diagonals Bottom Left -> Top Right" in {
          val game = new BoardTicTacToe()
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
