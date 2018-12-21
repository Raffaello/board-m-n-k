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
    var won: Option[Boolean] = Some(false)

    def inc(pos: Position, playerIdx: Int): Unit = {
      lastPlayerIdx = playerIdx
      rows(pos._1)(playerIdx) += 1
      assert(rows(pos._1)(playerIdx) <= m)
      cols(pos._2)(playerIdx) += 1
      assert(cols(pos._2)(playerIdx) <= n,  s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos -- ${board.flatten.mkString}")
      // TODO DIAGS1 and DIAG2
    }

    def dec(pos: Position, playerIdx: Int): Unit = {
//      assert(rows(p._1) > 0)
//      assert(cols(p._2) > 0)
//      assert(playerIdx == lastPlayerIdx)
      lastPlayerIdx = playerIdx
      rows(pos._1)(playerIdx) -= 1
      assert(rows(pos._1)(playerIdx) >= 0)
      cols(pos._2)(playerIdx) -= 1
      assert(cols(pos._2)(playerIdx) >= 0, s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos")
    }
  }

  val minWinDepth: Int = (2 * k) - 2 // 2*(k-1) // 2*k1 // zero-based depth require to subtract 1 extra more

  def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = position
//    require(row < m && col < n)
//    require(player <= numPlayers && player > 0)

    LookUps.won = None
    if (board(row)(col) > 0) false
    else {
      board(row)(col) = player
      freePositions -= 1
      lastMove = position
      lastPlayer = player
      LookUps.inc(position, player - 1)
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
  def undoMove(position: Position, player: Byte): Unit = {
    assert(board(position._1)(position._2) > 0)
    board(position._1)(position._2) = 0
    freePositions += 1
    LookUps.dec(position, player - 1)
    LookUps.won = None
    lastPlayer = player
  }

  override def score(): Int = ???

  protected def checkWin(): Boolean = ???

  override def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth){
      LookUps.won = Some(false)
      false
    }
    else if (freePositions == 0) {
      true
    }
    else checkWin()
  }
}
