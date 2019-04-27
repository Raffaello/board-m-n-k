package ai

import game.withBoard2D

import scala.collection.mutable

trait TranspositionTable extends withBoard2D {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  def hash() = _board.flatten.mkString

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
