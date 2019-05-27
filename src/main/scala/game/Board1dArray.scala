package game

import cats.implicits._

import scala.collection.immutable.NumericRange

abstract class Board1dArray(m: Short, n: Short) extends GameBoard {
  var _board: Board1d = Array.ofDim[Byte](m * n)

  protected def board(pos: Position): Byte = {
    val (i, j) = (pos.row, pos.col)
    _board(i * m + j)
  }

  protected def board_=(pos: Position)(p: Player): Unit = {
    val (i, j) = (pos.row, pos.col)
    _board(i * m + j) = p
  }

  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  final protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i * m + j) === 0
    } yield Position(i, j)
  }


  //  override def flatten: Board1d = _board
}
