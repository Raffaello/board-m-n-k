package game

abstract class BoardMN(val m: Short, val n: Short) {
  require(m > 2 && n > 2)
  val board: Board = Array.ofDim[Byte](m, n)
  val mnMin: Short = Math.min(m, n).toShort

  def playMove(position: Position, player: Byte): Boolean
  def undoMove(position: Position): Unit
  def score(): Int
  def gameEnded(): Boolean
}
