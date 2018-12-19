package game

abstract class BoardMN(val m: Short, val n: Short) {
  //  require(m > 2 && n > 2)

  val mnMin: Short = Math.min(m, n).toShort
  protected val board: Board = Array.ofDim[Byte](m, n)
  def getBoard(): Board = board
  protected var freePositions: Int = m * n
  protected var lastMove: Position = (0, 0)

  def move(position: Position) = board(position._1)(position._2)
  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position): Unit

  def score(): Int

  def gameEnded(depth: Int): Boolean
}
