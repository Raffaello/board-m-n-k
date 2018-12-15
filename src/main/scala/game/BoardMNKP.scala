package game

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(val m: Short, val n: Short, val k: Short, val numPlayers: Byte) {
  require(m > 2 && n > 2 && k > 2)
  require(k <= m || k <= n)
  require(numPlayers >= 2)

  val board: Board = Array.ofDim[Byte](m, n)
  val mnMin: Short = Math.min(m, n).toShort

  def playMove(position: Position, player: Byte): Boolean = {
    val row = position._1
    val col = position._2
    require(row < m && col < n)
    require(player <= numPlayers && player > 0)

    if (board(row)(col) > 0) {
      false
    } else {
      board(row)(col) = player
      true
    }
  }

  def playMove(row: Short, col: Short, player: Byte): Boolean = playMove((row, col), player)

  def undoMove(position: Position): Unit = board(position._1)(position._2) = 0
}
