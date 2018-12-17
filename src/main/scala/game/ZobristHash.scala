package game

class ZobristHash {
  type ZobristTable = Array[Array[Array[Int]]]

  var zobristKey = 0
  val table:ZobristTable  = ???
  def init() = ???

  def hash(board: Board) = ???
}
