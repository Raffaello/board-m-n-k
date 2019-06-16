package ai

import cats.implicits._
import game.Score
import game.types.{Position, Status}

/**
  * Basically same class as
  *
  * @see MiniMax
  */
trait NegaMax extends AiBoard with AiBoardScoreEval {
  protected def mainBlock(color: Byte)(eval: Status[Score] => Score): Score = {
    if (gameEnded()) {
      scoreEval(_lastPlayer) * color
    } else {
      Stats.totalCalls += 1
      var value = Int.MinValue
      val player: Byte = if (color == -1) 2 else 1

      consumeMoves { p =>
        playMove(p, player)
        value = eval(Status(value, p))
        undoMove(p, player)
      }

      value
    }
  }

  def solve(color: Byte): Score = {
    mainBlock(color) { status: Status[Score] =>
      Math.max(status.score, -solve((-color).toByte))
    }
  }

  def solve: Score = solve(1)

  override def nextMove: Status[Score] = nextMove(if (nextPlayer() === aiPlayer) 1 else -1)

  protected def nextMove(color: Byte): Status[Score] = {
    // todo made mainBlock returning more than just the score
    var pBest: Position = Position.nil
    val score = mainBlock(color) { status: Status[Score] =>
      val newValue = -solve((-color).toByte)
      if (status.score < newValue) {
        pBest = status.position
        newValue
      } else status.score
    }

    Status(score, pBest)
  }
}
