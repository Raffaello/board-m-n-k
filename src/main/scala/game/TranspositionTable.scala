package game

import scala.collection.mutable

trait TranspositionTable /*extends BoardMN*/ {
  val transpositions: mutable.Map[String, Transposition] = mutable.Map.empty
/*
  def hash() = board.map(_.mkString("")).mkString("")

  def add(t: Transposition): Unit = {
    transpositions.update(hash(), t)
  }

  def upd(t: Transposition) = {
    transpositions.update(hash(), t)
  }

  def del(): Option[Transposition] = {
    transpositions.remove(hash())
  }

  def get(): Option[Transposition] = {
    transpositions.get(hash())
  }
*/

  /**
    * @deprecated
    */
  def hash(b: Board) = b.map(_.mkString("")).mkString("")

  /**
    * @deprecated
    */
  def add(b: Board, t: Transposition): Unit = {
    transpositions.update(hash(b), t)
  }

  /**
    * @deprecated
    */
  def get(b: Board): Option[Transposition] = {
    transpositions.get(hash(b))
  }
}
