package cakes.ai.old

import game.Board2d

import scala.collection.mutable

/**
  * @deprecated
  */
trait TranspositionTable {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty

  def hash(b: Board2d): String = b.flatten.mkString

  def add(b: Board2d, t: Transposition): Unit = {
    transpositions.update(hash(b), t)
  }

  def get(b: Board2d): Option[Transposition] = {
    transpositions.get(hash(b))
  }
}
