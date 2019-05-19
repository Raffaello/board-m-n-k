package game

import cats.implicits._

import scala.collection.immutable.NumericRange

abstract class Board1dArray(m: Short, n: Short) extends GameBoard {
  protected var _board: Board1d = Array.ofDim[Byte](m * n)

  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  final protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i * m + j) === 0
    } yield (i, j)
  }

  protected def board(pos: Position): Byte = {
    val (i, j) = pos
    _board(i * m + j)
  }

  protected def board(pos: Position)(p: Player): Unit = {
    val (i, j) = pos
    _board(i * m + j) = p
  }

//  override def flatten: Board1d = _board
}
