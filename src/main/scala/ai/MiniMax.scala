package ai

import game.{Position, Status}

trait MiniMax extends AiBoard {

  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  /**
    * @deprecated used for benchmarks on FP and code reuse too
    * @param maximizing
    * @param depth
    * @return
    */
  def solveRaw(maximizing: Boolean = true, depth: Int = 0): Int = {
    if (gameEnded(depth)) {
      score()
    } else {
      Stats.totalCalls += 1
      if (maximizing) {
        var value = Int.MinValue

        consumeMoves() { p =>
          playMove(p, 1)
          value = Math.max(value, solve(!maximizing, depth + 1))
          undoMove(p, 1)
        }

        value
      } else {
        var value = Int.MaxValue

        consumeMoves() { p =>
          playMove(p, 2)
          value = Math.min(value, solve(!maximizing, depth + 1))
          undoMove(p, 2)
        }

        value
      }
    }
  }

  protected def mainBlock(player: Byte, depth: Int)(eval: Status => Int): Int = {
    if (gameEnded(depth)) {
      score()
    } else {
      Stats.totalCalls += 1
      var value = if (player == 1) Int.MinValue else Int.MaxValue

      consumeMoves() { p =>
        playMove(p, player)
        value = eval((value, p))
        undoMove(p, player)
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
      val newValue = solve(!maximizing, depth + 1)
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
