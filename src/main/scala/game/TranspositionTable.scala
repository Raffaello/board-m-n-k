package game

import scala.collection.mutable


trait TranspositionTable {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  def hash(board: Board) = board.map(_.mkString("")).mkString("")

  def add(board: Board, t: Transposition) = {
    transpositions.getOrElseUpdate(hash(board), t)
  }

  def del(board: Board): Option[Transposition] = {
    transpositions.remove(hash(board))
  }

  def get(board: Board): Option[Transposition] = {
    transpositions.get(hash(board))
  }
}
