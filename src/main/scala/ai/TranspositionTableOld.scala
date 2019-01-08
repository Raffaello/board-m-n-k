package ai

import game.Board

import scala.collection.mutable

trait TranspositionTableOld /*extends BoardMN*/ {
  val transpositions: mutable.Map[String, TranspositionOld] = mutable.Map.empty

//  var zobristKey:Int = 0

//  def init(b: BoardMN): ZobristTable = Array.fill[Int](b.m, b.n, 2)(Random.nextInt())

  /**
    * To call for each play move and undo move
    */
//  def hash(zobristTable: ZobristTable, p: Position, player: Byte): Int = {
//    zobristKey ^= zobristTable(p._1)(p._2)(player)
//    zobristKey
//  }
//
//  def add(z: ZobristTable, pos: Position, p: Byte, t: Transposition): Unit = {
//    transpositions.update(hash(z, pos , p), t)
//  }
//
//  def get(z: ZobristTable, pos: Position, p: Byte, t: Transposition): Option[Transposition] = {
//    transpositions.get(hash(z, pos, p))
//  }
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
  def hash(b: Board) = b.flatten.mkString

  /**
    * @deprecated
    */
  def add(b: Board, t: TranspositionOld): Unit = {
    transpositions.update(hash(b), t)
  }

  /**
    * @deprecated
    */
  def get(b: Board): Option[TranspositionOld] = {
    transpositions.get(hash(b))
  }
}
