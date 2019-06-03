package game.boards

import game.Player
import game.types.{BoardMNType, Position}

trait BoardDisplay extends BoardMNType {

  private[this] def value(player: Player): Char = {
    player match {
      case 0 => '_'
      case x => x.toChar
    }
  }

  def stdoutPrintln(): Unit = println(display())

  def display(): String = {
    val str: StringBuilder = new StringBuilder()
    val newLine = sys.props("line.separator")
    val n1 = n - 1

    for (i <- mIndices) {
      for (j <- 0 until n1) {
        str ++= s" ${value(boardPlayer(Position(i.toShort, j.toShort)))} |"
      }

      str ++= s" ${value(boardPlayer(Position(i.toShort, n1.toShort)))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}
