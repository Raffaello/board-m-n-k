package ai.old

import game.Board

import scala.collection.mutable

trait TranspositionTable {
  val transpositions: mutable.Map[String, TranspositionOld] = mutable.Map.empty

  def hash(b: Board): String = b.flatten.mkString

  def add(b: Board, t: TranspositionOld): Unit = {
    transpositions.update(hash(b), t)
  }

  def get(b: Board): Option[TranspositionOld] = {
    transpositions.get(hash(b))
  }
}
