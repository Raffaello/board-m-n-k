package ai

import game.Board2D

class ZobristHash {
  type ZobristTable = Array[Array[Array[Int]]]

  var zobristKey = 0
  val table:ZobristTable  = ???
  def init() = ???

  def hash(board: Board2D) = ???
}
