package ai.old

import game.Board

import scala.collection.mutable

trait TranspositionTable {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  def hash(b: Board): String = b.flatten.mkString

  def add(b: Board, t: Transposition): Unit = {
    transpositions.update(hash(b), t)
  }

  def get(b: Board): Option[Transposition] = {
    transpositions.get(hash(b))
  }
}
