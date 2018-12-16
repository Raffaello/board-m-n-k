package game

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n) {
  require(k > 2)
  require(k <= m || k <= n)
  require(numPlayers >= 2)

  def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = position
    require(row < m && col < n)
    require(player <= numPlayers && player > 0)

    if (board(row)(col) > 0) {
      false
    } else {
      board(row)(col) = player
      true
    }
  }

  /**
    * @deprecated
    */
  def playMove(row: Short, col: Short, player: Byte): Boolean = playMove((row, col), player)

  def undoMove(position: Position): Unit = board(position._1)(position._2) = 0

  override def score(): Int = ???

  override def gameEnded(): Boolean = ???
}
