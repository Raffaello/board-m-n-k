package ai

import game.{Position, Status}

trait MiniMax extends AiBoard {

  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  protected def mainBlock(player: Byte, depth: Int)(eval: Status => Int): Int = {
    if (gameEnded(depth)) {
      score()
    } else {
      var value = if (player == 1) Int.MinValue else Int.MaxValue

      consumeMoves() { p =>
        playMove(p, player)
        value = eval((value, p))
        undoMove(p)
      }

      value
    }
  }

  /**
    * TODO: Replace maximizing with player directly, player 1 always maximizing
    * or just consider player 2 as all of the opponents.
    * Only for 2 players at the moment
    */
  def solve(maximizing: Boolean = true, depth: Int = 0): Int = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max _ else Math.min _

    mainBlock(player(maximizing), depth) { status =>
      val value = solve(!maximizing, depth + 1)
      cmp(status._1, value)
    }
  }

  def nextMove(maximizing: Boolean, depth: Int): Status = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(player(maximizing), depth) { status =>
      val newValue = -solve(!maximizing, depth + 1)
      var value = status._1
      if (maximizing) {
        if (value < newValue) {
          value = newValue
          pBest = status._2
        }
      } else {
        if (value > newValue) {
          value = newValue
          pBest = status._2
        }
      }

      value
    }

    (score, pBest)
  }
}
