package ai

import game.BoardMN

import scala.collection.mutable

trait TranspositionTable extends BoardMN {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  def hash(): String = board.flatten.mkString

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
