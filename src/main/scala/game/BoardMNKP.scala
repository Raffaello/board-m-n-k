package game

/**
  * @param m          number of rows
  * @param n          number of cols
  * @param k          number of same move of a player "in a row" (or col or diagonal)
  * @param numPlayers 0 is not used, 1 or 2 is the player using the cell
  */
class BoardMNKP(val m: Short, val n: Short, val k: Short, val numPlayers: Byte) {
  require(m > 0 && n > 0 && k > 0)
  require(k <= m || k <= n)
  require(numPlayers >= 2)

  val board: Array[Array[Byte]] = Array.ofDim[Byte](m, n)

  val mnMin: Short = Math.min(m, n).toShort

  /**
    * this is only for human player in theory at the moment...
    *
    * @param  row
    * @param  col
    * @param  player
    * @return true if valid move false otherwise
    */
  def playMove(row: Short, col: Short, player: Byte): Boolean = {
    require(row < m && col < n)
    require(player <= numPlayers && player > 0)

    if (board(row)(col) > 0) {
      false
    } else {
      board(row)(col) = player
      true
    }
  }

  def undoMove(row: Short, col: Short): Unit = board(row)(col) = 0

  /**
    * @deprecated
    * @return Some((row, col)) index  OR None
    */
//  def findFirstEmptyCell(): Option[(Short, Short)] = {
//    for {
//      i <- 0 until m
//      j <- 0 until n
//      if board(i)(j) == 0
//    } {
//      return Some((i.toShort, j.toShort))
//    }
//
//    None
//  }

//  protected def checkWinRows(): Boolean = {
//    for {
//      i <- 0 until m
//      p <- 1 to numPlayers
//      if scoreRow(i.toShort, p.toByte) > 0
//    } {
//      return true
//    }
//
//    false
//  }
//
//  protected def checkWinCols(): Boolean = {
//    for {
//      j <- 0 until n
//      p <- 1 to numPlayers
//      if scoreCol(j.toShort, p.toByte) > 0
//    } {
//      return true
//    }
//
//    false
//  }
//
//  /**
//    * @todo cache the current board state for the wining, optimizing the method. for now is recomputing everytime,
//    *       *       would be better store with a delta change for each move.
//    * @return
//    */
//  protected def checkWinDiagonals(): Boolean = {
//    for {
//      i <- 0 until m
//      p <- 1 to numPlayers
//      if scoreDiagTL(i.toShort, p.toByte) > 0 || scoreDiagBR(i.toShort, p.toByte) > 0
//    } {
//      return true
//    }
//
//    false
//  }
//
//  protected def checkWin(): Boolean = checkWinRows() || checkWinCols() || checkWinDiagonals()

//  protected def scoreRow(row: Short, p: Byte): Int = {
//    var countK = 0
//    for(j <- 0 until n) {
//      if (board(row)(j) == p) {
//        countK += 1
//      }
//    }
//
//    if (countK == k) 1 else 0
//  }

//  protected def scoreRows(player: Byte): Int = (0 until m).foldLeft(0)((acc, i) => acc + scoreRow(i.toShort, player))

//  protected def scoreCol(col: Short, p: Byte): Int = {
//    var countK = 0
//    for(i <- 0 until m) {
//      if(board(i)(col) == p) {
//        countK += 1
//      }
//    }
//
//    if (countK == k) 1 else 0
//  }
//
//  protected def scoreCols(player: Byte): Int = (0 until n).foldLeft(0)((acc, j) => acc + scoreCol(j.toShort, player))

  /**
    * @TODO NOT WORKING FOR weird diagonal when k < m or n
    * @param r
    * @param p
    * @return
    */
//  protected def scoreDiagTL(r: Short, p: Byte): Int = {
//    if (m - r < k) {
//      return 0
//    }
//
//    var countK = 0
//    for(i <- r until mnMin) {
//      if (board(i)(i) == p) {
//        countK += 1
//      }
//    }
//
//    if (countK == k) 1 else 0
//  }

//  protected def scoreDiagsTL(player: Byte): Int = (0 until m).foldLeft(0)((acc, i) => acc + scoreDiagTL(i.toShort, player))

  /**
    * @TODO NOT WORKING FOR weird diagonal when k < m or n
    * @param r
    * @param p
    * @return
    */
//  protected def scoreDiagBR(r: Short, p: Byte): Int = {
//    if (r < k-1) {
//      return 0
//    }
//
//    var countK = 0
//    for(i <- r to 0 by -1) {
//      if(board(i)(i) == p) {
//        countK += 1
//      }
//    }
//
//    if (countK == k) 1 else 0
//  }

//  protected def scoreDiagsBR(player:Byte): Int = (0 until m).foldLeft(0)((acc, i) => acc + scoreDiagBR(i.toShort, player))

//  def score(player: Byte): Int = {
//    scoreRows(player) + scoreCols(player) + scoreDiagsTL(player) + scoreDiagsBR(player)
//  }

//  def ended(): Boolean = {
//    if (!checkWin()) {
//      for {
//        i <- 0 until m
//        j <- 0 until n
//        if board(i)(j) == 0
//      } {
//        return false
//      }
//    }
//
//    true
//  }
}
