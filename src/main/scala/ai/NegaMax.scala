package ai

import game.{Status, Position}

/**
  * Basically same class as
  * @see MiniMax
  */
trait NegaMax extends AiBoard {

  private def mainBlock(color: Byte)(eval: Status[Int] => Int): Int = {
    if (gameEnded()) {
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

  def solve(color: Byte): Int = {
    require(color == 1 || color == -1)

    mainBlock(color) { status =>
      val newValue = -solve((-color).toByte)
      Math.max(status._1, newValue)
    }
  }

  def nextMove(color: Byte): Status[Int] = {
    require(color == 1 || color == -1)

    var pBest: Position = (-1, -1)
    val score = mainBlock(color) { status =>
      val newValue = -solve(color)
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
