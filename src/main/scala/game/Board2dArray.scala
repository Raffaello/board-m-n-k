package game

import cats.implicits._
import game.boards.BoardT
import game.types.{BoardMNType2dArray, Position}

// TODO: decouple the 2 used traits if it is possible. Type Classes?
trait Board2dArray extends BoardMNType2dArray with BoardT {
  @inline
  protected def board(pos: Position): Byte = _board(pos.row)(pos.col)

  @inline
  protected def board_=(pos: Position)(p: Player): Unit = _board(pos.row)(pos.col) = p


  override protected def board: Board2d = _board

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(Position(i, j)) === 0
    } yield Position(i, j)
  }

  def display(): String = {
    val str: StringBuilder = new StringBuilder()
    val newLine = sys.props("line.separator")

    def value(p: Byte): Char = {
      p match {
        case 0 => '_'
        case 1 => 'X'
        case 2 => 'O'
        case _ => ???
      }
    }

    for (i <- mIndices) {
      for (j <- 0 until n - 1) {
        str ++= s" ${value(board(Position(i.toShort, j.toShort)))} |"
      }

      str ++= s" ${value(board(Position(i.toShort, (n - 1).toShort)))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}
