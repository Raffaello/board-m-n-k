package ai

import cats.implicits._
import game.{Position, Score, Status}

/**
  * Basically same class as
  *
  * @see MiniMax
  */
trait NegaMax extends AiBoard with AiBoardScoreEval {
  protected def mainBlock(color: Byte)(eval: Status => Score): Score = {
    if (gameEnded()) {
      scoreEval * color
    } else {
      Stats.totalCalls += 1
      var value = Int.MinValue
      val player: Byte = if (color == -1) 2 else 1

      consumeMoves() { p =>
        playMove(p, player)
        value = eval((value, p))
        undoMove(p, player)
      }

      value
    }
  }

  def solve(color: Byte): Score = {
    mainBlock(color) { case (score, _) =>
      Math.max(score, -solve((-color).toByte))
    }
  }

  def solve: Score = solve(1)

  override def nextMove: Status = nextMove(if (nextPlayer() === aiPlayer) 1 else -1)

  protected def nextMove(color: Byte): Status = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(color) { case (score: Score, pos: Position) =>
      val newValue = -solve((-color).toByte)
      if (score < newValue) {
        pBest = pos
        newValue
      } else score
    }

    (score, pBest)
  }
}
