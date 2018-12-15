package ai

import game.{Status, Position}

trait NegaMax extends AiBoard {

  private def mainBlock(color: Byte)(eval: Status[Int] => Int): Int = {
    if (game.ended()) {
      return endGame[Int](score => score * color)
    }

    var value = Int.MinValue
    val player: Byte = if (color == -1) 2 else 1

    consumeMoves() { p =>
      game.playMove(p, player)
      value = eval((value, p))
      game.undoMove(p)
    }

    value
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
