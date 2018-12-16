package ai

import game.{Status, Position}

/**
  * Basically same class as
  * @see MiniMax
  */
trait NegaMax extends AiBoard {

  private def mainBlock(color: Byte, depth: Int)(eval: Status[Int] => Int): Int = {
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

  def solve(color: Byte, depth: Int): Int = {
    require(color == 1 || color == -1)

    mainBlock(color, depth) { status =>
      val newValue = -solve((-color).toByte, depth + 1)
      Math.max(status._1, newValue)
    }
  }

  def nextMove(color: Byte, depth: Int): Status[Int] = {
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
