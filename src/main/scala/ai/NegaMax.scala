package ai

import cats.implicits._
import game.{Position, Score, Status}

/**
  * Basically same class as
  *
  * @see MiniMax
  */
trait NegaMax extends AiBoard {
  protected def mainBlock(color: Byte, depth: Int)(eval: Status => Score): Score = {
    if (gameEnded(depth)) {
      score() * color
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

  def solve(color: Byte, depth: Int): Score = {
    mainBlock(color, depth) { status =>
      Math.max(status._1, -solve((-color).toByte, depth + 1))
    }
  }

  def solve: Score = solve(1, 0)

  override def nextMove: Status = nextMove(if (nextPlayer() === aiPlayer) 1 else -1, depth)

  protected def nextMove(color: Byte, depth: Int): Status = {
    var pBest: Position = (-1, -1)
    val score = mainBlock(color, depth) { case (score: Score, pos: Position) =>
      val newValue = -solve((-color).toByte, depth + 1)
      if (score < newValue) {
        pBest = pos
        newValue
      } else score
    }

    (score, pBest)
  }
}
