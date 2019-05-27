package ai

import cats.implicits._
import game.{BoardMNK, Position, Score, Status}

trait MiniMax extends BoardMNK with AiBoard with AiBoardScoreEval {
  protected def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

  protected def mainBlock(player: Byte)(eval: Status => Score): Score = {
    if (gameEnded()) {
      scoreEval
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
  def solve(maximizing: Boolean): Score = {
    val cmp: (Int, Int) => Int = if (maximizing) Math.max else Math.min

    mainBlock(player(maximizing)) { status =>
      val value = solve(!maximizing)
      cmp(status._1, value)
    }
  }

  def solve: Score = solve(nextPlayer() === aiPlayer)

  private[this] def signum(value: Boolean): Byte = if (value) +1 else -1

  override def nextMove: Status = nextMove(nextPlayer() === aiPlayer)

  protected def nextMove(maximizing: Boolean): Status = {
    var pBest: Position = Position(-1, -1)
    val score = mainBlock(player(maximizing)) { case (score: Score, pos: Position) =>
      val newValue = solve(!maximizing)
      val sig = signum(maximizing)
      if (score * sig < newValue * sig) {
        pBest = pos
        newValue
      } else score
    }

    (score, pBest)
  }
}
