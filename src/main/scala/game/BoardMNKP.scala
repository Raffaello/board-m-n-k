package game

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Byte) extends BoardMN(m, n) {
//  require(k > 2)
//  require(k <= m || k <= n)
//  require(numPlayers >= 2)

  protected var lastPlayer: Byte = 0

  object LookUps {
    val rows: Array[Array[Int]] = Array.ofDim[Int](m, numPlayers)
    val cols: Array[Array[Int]] = Array.ofDim[Int](n, numPlayers)
    val diag1: Array[Array[Int]] = Array.ofDim[Int](mnMin, numPlayers)
    val diag2: Array[Array[Int]] = Array.ofDim[Int](mnMin, numPlayers)
    var lastPlayerIdx: Int = 0
    var won: Boolean = false

    def inc(pos: Position, player: Int): Unit = {
      lastPlayerIdx = player
      rows(pos._1)(player) += 1
      cols(pos._2)(player) += 1
      // TODO DIAGS1 and DIAG2
    }

    def dec(pos: Position): Unit = {
//      assert(rows(p._1) > 0)
//      assert(cols(p._2) > 0)
      rows(pos._1)(lastPlayerIdx) -= 1
      cols(pos._2)(lastPlayerIdx) -= 1
    }
  }

  val minWinDepth: Int = (2 * k) - 1

  def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = position
//    require(row < m && col < n)
//    require(player <= numPlayers && player > 0)

    if (board(row)(col) > 0) false
    else {
      board(row)(col) = player
      freePositions -= 1
      lastMove = position
      lastPlayer = player
      LookUps.inc(position, player -1)
      true
    }
  }

  /**
    * @deprecated
    */
  def playMove(row: Short, col: Short, player: Byte): Boolean = playMove((row, col), player)

  /**
    * TODO: should check if it is not already zero otherwise
    *       freePositions should not be incremented
    */
  def undoMove(position: Position): Unit = {
//    assert(board(position._1)(position._2) > 0)
    board(position._1)(position._2) = 0
    freePositions += 1
    LookUps.dec(position)
  }

  override def score(): Int = ???

  protected def checkWin(): Boolean = ???

  override def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions == 0) true
    else checkWin()
  }
}
