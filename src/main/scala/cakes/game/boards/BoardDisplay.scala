package cakes.game.boards

import cakes.game.Player
import cakes.game.types.BoardMNType
import types.Position
import types.implicits.convertIntToShort

trait BoardDisplay extends BoardMNType {

  private[boards] def valueDisplay(player: Player): Char = {
    player match {
      case 0 => '_'
      case x => Character.forDigit(x, 10)
    }
  }

  def stdoutPrintln(): Unit = println(display())

  def display(): String = {
    val str: StringBuilder = new StringBuilder()
    val newLine = sys.props("line.separator")
    val n1 = n - 1

    for (i <- mIndices) {
      for (j <- 0 until n1) {
        str ++= s" ${valueDisplay(boardPlayer(Position(i, j)))} |"
      }

      str ++= s" ${valueDisplay(boardPlayer(Position(i, n1)))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}
