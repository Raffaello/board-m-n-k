package ai

import game.{Position, Status}

trait MiniMax extends AiBoard {

  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  protected def mainBlock(player: Byte, depth: Int)(eval: Status[Int] => Int): Int = {
    if (gameEnded(depth)) {
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
  def solve(maximizing: Boolean, depth: Int): Int = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max _ else Math.min _

    mainBlock(player(maximizing), depth) { status =>
      val value = solve(!maximizing, depth + 1)
      cmp(status._1, value)
    }
  }

  def nextMove(maximizing: Boolean, depth: Int): Status[Int] = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(player(maximizing), depth) { status =>
      val newValue = -solve(!maximizing, depth + 1)
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
