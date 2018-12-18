package ai

import game.{Status, Position}

/**
  * Basically same class as
  * @see MiniMax
  */
trait NegaMax extends AiBoard {

  private def mainBlock(color: Byte, depth: Int)(eval: Status => Int): Int = {
    if (gameEnded(depth)) {
      score() * color
    } else {

      var value = Int.MinValue
      val player: Byte = if (color == -1) 2 else 1

      consumeMoves() { p =>
        playMove(p, player)
        value = eval((value, p))
        undoMove(p)
      }

      value
    }
  }

  def solve(color: Byte = 1, depth: Int = 0): Int = {
    require(color == 1 || color == -1)

    mainBlock(color, depth) { status =>
      Math.max(status._1, -solve((-color).toByte, depth + 1))
    }
  }

  def nextMove(color: Byte, depth: Int): Status = {
    require(color == 1 || color == -1)

    var pBest: Position = (-1, -1)
    val score = mainBlock(color, depth) { status =>
      val newValue = -solve(color, depth + 1)
      var value = status._1
      if (value < newValue) {
        value = newValue
        pBest = status._2
      }

      value
    }

    (score, pBest)
  }
}
