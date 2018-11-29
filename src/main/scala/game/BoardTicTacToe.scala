package game

final class BoardTicTacToe extends BoardMNK(3, 3, 3) {
  val m = 3
  val n = 3
  val k = 3
  val numPlayers = 2

  def display(): Unit = {
    def value(p: Byte): Char = {
      p match {
        case 0 => '_'
        case 1 => 'X'
        case 2 => 'O'
      }
    }

    for (i <- 0 until m) {
      for (j <- 0 until n-1) {
        print(s" ${value(board(i)(j))} |")
      }
      println(s" ${value(board(i)(n-1))}")
    }

    println()
  }

  protected def scoreDiagTL(p: Byte): Int = {
    var countK = 0
    for(i <- 0 until m) {
      if (board(i)(i) == p) {
        countK += 1
      }
    }

    if (countK == k) 1 else 0
  }

  override protected def scoreDiagsTL(player: Byte): Int = scoreDiagTL(player)

  protected def scoreDiagBR(p: Byte): Int = {
    var countK = 0
    for(i <- 0 until m) {
      if(board(m-1-i)(i) == p) {
        countK += 1
      }
    }

    if (countK == k) 1 else 0
  }

  override protected def scoreDiagsBR(player:Byte): Int = scoreDiagBR(player)

  override protected def checkWinDiagonals(): Boolean = {

    for {
      p <- 1 to numPlayers
      if scoreDiagTL(p.toByte) > 0 || scoreDiagBR(p.toByte) > 0
    } {
      return true
    }

    false
  }
}
