package ai

import game.BoardMNKP
import game.boards.concrete.Board2dArray

import scala.collection.mutable

// TODO: Use a more performant hash
// TODO: at the moment force 2d array due to _board as a 2d array.. how to solve it??
trait TranspositionTable extends BoardMNKP with Board2dArray {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  // TODO can be done only with array 2d at the moment
  def hash(): String = _board.flatten.mkString

  def add(t: Transposition): Unit = {
    transpositions.update(hash(), t)
  }

  def del(): Option[Transposition] = {
    transpositions.remove(hash())
  }

  def get(): Option[Transposition] = {
    transpositions.get(hash())
  }
}
