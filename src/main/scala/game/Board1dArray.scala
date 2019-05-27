package game

import cats.implicits._
import game.boards.BoardT
import game.types.{BoardMNType1dArray, Position}

trait Board1dArray extends BoardMNType1dArray with BoardT {

  override def display(): String = ???

  override protected def board: Board1d = _board

  protected def board(pos: Position): Byte = {
    // TODO look up i*m as a im
    _board(mLookups(pos.row) + pos.col)
  }

  protected def board_=(pos: Position)(p: Player): Unit = {
    val (i, j) = (pos.row, pos.col)
    _board(i * m + j) = p
  }

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i * m + j) === 0
    } yield Position(i, j)
  }


  //  override def flatten: Board1d = _board
}
