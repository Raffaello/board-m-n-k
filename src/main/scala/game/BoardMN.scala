package game

import scala.collection.immutable.NumericRange

abstract class BoardMN(val m: Short, val n: Short) {
  //  require(m > 2 && n > 2)

//  val mnMin: Short = Math.min(m, n).toShort
  val mIndices: NumericRange[Short] = NumericRange[Short](0, m, 1)
  val nIndices: NumericRange[Short] = NumericRange[Short](0, n, 1)

  protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if _board(i)(j) == 0
    } yield (i,j)
  }

  protected def consumeMoves()(f: Position => Unit): Unit = generateMoves().foreach(f)

  protected val _board: Board = Array.ofDim[Byte](m, n)
//  def board(): Board = _board

  protected var freePositions: Int = m * n
  protected var lastMove: Position = (0, 0)

//  def move(position: Position): Byte = _board(position._1)(position._2)

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position, player: Byte): Unit

  def score(): Int

  def gameEnded(): Boolean
}
