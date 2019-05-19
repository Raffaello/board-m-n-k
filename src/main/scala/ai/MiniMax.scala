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
  def solve(maximizing: Boolean, depth: Int): Score = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min

    mainBlock(player(maximizing), depth) { status =>
      val value = solve(!maximizing, depth + 1)
      cmp(status._1, value)
    }
  }

  def solve: Score = solve(true, 0)

  private[this] def signum(value: Boolean): Byte = if (value) +1 else -1

  def nextMove(maximizing: Boolean, depth: Int): Status = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(player(maximizing), depth) { case (score: Score, pos: Position) =>
      val newValue = solve(!maximizing, depth + 1)
      val sig = signum(maximizing)
      if(score * sig < newValue * sig) {
        pBest = pos
        newValue
      } else score
    }

    (score, pBest)
  }
}
