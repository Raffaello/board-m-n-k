package game

class BoardConnectMN(m: Short, n: Short, k: Short) extends BoardMN(m, n) {

  override def playMove(position: (Short, Short), player: Byte): Boolean =  ???

  override def undoMove(position: (Short, Short), player: Byte): Unit = ???

  override def score(): Int = ???

  override def gameEnded(depth: Int): Boolean = ???
}
