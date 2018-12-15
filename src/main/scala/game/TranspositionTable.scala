package game

import scala.collection.mutable


trait TranspositionTable {
//  val board: Board
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

//  def hash() = board.map(_.mkString("")).mkString("")
  def hash(board: Board) = board.map(_.mkString("")).mkString("")

  def add(board: Board, t: Transposition): Unit = {
//    require(t.score>=t.alpha)
//    require(t.score<=t.beta)
//    require(t.alpha<=t.beta)
    transpositions.update(hash(board), t)
  }

  def upd(board: Board, t: Transposition) = {
    transpositions.update(hash(board), t)
  }

  def del(board: Board): Option[Transposition] = {
    transpositions.remove(hash(board))
  }

  def get(board: Board): Option[Transposition] = {
    transpositions.get(hash(board))
  }
}
