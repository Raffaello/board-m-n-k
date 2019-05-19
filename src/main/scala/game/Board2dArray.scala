package game

import cats.implicits._

import scala.collection.immutable.NumericRange

abstract class Board2dArray(m: Short, n: Short) extends GameBoard {
  protected var _board: Board2d = Array.ofDim[Byte](m, n)

  protected def board(pos: Position): Byte = {
    val (i, j) = pos
    _board(i)(j)
  }

  def setBoard(pos: Position, p: Player): Unit = {
    val (i, j) = pos
    _board(i)(j) = p
  }

  protected def board(pos: Position)(p: Player): Unit = {
    val (i, j) = pos
    _board(i)(j) = p
  }

  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  final protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i)(j) === 0
    } yield (i, j)
  }

//  override def flatten: Board1d = _board.flatten
}
