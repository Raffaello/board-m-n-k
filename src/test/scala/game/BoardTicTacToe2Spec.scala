package game

import game.types.Position
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable.NumericRange

class BoardTicTacToe2Spec extends WordSpec with Matchers {

  "A Game" should {
    "in progress" in {
      val game = new BoardTicTacToe2()

      game.gameEnded(0) should be(false)
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
            val game = new BoardTicTacToe2()
            for(j <- 0 until  3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        "by cols" in {
          for (j <- 0 until 3) {
            val game = new BoardTicTacToe2()
            for (i <- 0 until 3) game.playMove(Position(i.toShort, j.toShort), p)
            game.gameEnded() should be(true)
            game.score() should be(score)
          }
        }

        "by Diagonals Top Left -> Bottom Right" in {
          val game = new BoardTicTacToe2()
          for (i <- 0 until 3) game.playMove(Position(i.toShort, i.toShort), p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }

        "by diagonals Bottom Left -> Top Right" in {
          val game = new BoardTicTacToe2()
          for (i <- 0 until 3) game.playMove(Position((2 - i).toShort, i.toShort),  p)
          game.gameEnded() should be(true)
          game.score() should be(score)
        }
      }
    }
  }
}
