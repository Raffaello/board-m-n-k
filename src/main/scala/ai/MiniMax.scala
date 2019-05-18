package ai

import game.{BoardMNK, Position, Score, Status}

trait MiniMax extends BoardMNK with AiBoard {
  protected def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  protected def mainBlock(player: Byte, depth: Int)(eval: Status => Score): Score = {
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
    * Only for 2 players at the moment
    */
  def solve(maximizing: Boolean = true, depth: Int = 0): Score = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min

    mainBlock(player(maximizing), depth) { status =>
      val value = solve(!maximizing, depth + 1)
      cmp(status._1, value)
    }
  }

  def solve: Score = solve()

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
