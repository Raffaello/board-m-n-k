package ai

import game.BoardMN

import scala.collection.mutable

// TODO: Use a more performant hash
trait TranspositionTable extends BoardMN {
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
