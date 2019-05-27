package game

import cats.implicits._

import scala.collection.immutable.NumericRange

trait Board2dArray extends BoardMNType {
  protected var _board: Board2d = Array.ofDim[Byte](m, n)

  // actual hack for the transposition
  // protected def board:Board2d = _board
  @inline
  protected def board(pos: Position): Byte = _board(pos.row)(pos.col)

  @inline
  protected def board_=(pos: Position)(p: Player): Unit = _board(pos.row)(pos.col) = p

  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(Position(i, j)) === 0
    } yield Position(i, j)
  }

//  override def flatten: Board1d = _board.flatten

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

      str ++= s" ${value(board(Position(i.toShort, (n-1).toShort)))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}
