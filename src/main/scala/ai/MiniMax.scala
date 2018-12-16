package ai

import game.{Position, Status}

trait MiniMax extends AiBoard {

  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  protected def mainBlock(player: Byte)(eval: Status[Int] => Int): Int = {
    if (gameEnded()) {
      score()
    } else {
      var value = Int.MinValue

      consumeMoves() { p =>
        playMove(p, player)
        value = eval((value, p))
        undoMove(p)
      }

      value
    }
  }

  /**
    * Only for 2 players at the moment
    */
  def solve(maximizing: Boolean): Int = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max _ else Math.min _

    mainBlock(player(maximizing)) { status =>
      val value = solve(!maximizing)
      cmp(status._1, value)
    }
  }

  def nextMove(maximizing: Boolean): Status[Int] = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(player(maximizing)) { status =>
      val newValue = -solve(!maximizing)
      var value = status._1
      if (maximizing) {
        if (value > newValue) {
          value = newValue
          pBest = status._2
        }
      } else {
        if (value < newValue) {
          value = newValue
          pBest = status._2
        }
      }

      value
    }

    (score, pBest)
  }
}
