package cakes.ai

import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.{BitBoard, BoardMNKP}

import scala.collection.mutable

trait TranspositionTable extends BoardMNKP {
  type T
  val transpositions: mutable.Map[T, Transposition] = mutable.Map.empty

  def hash: T

  def add(t: Transposition): Unit = {
    transpositions.update(hash, t)
  }

  def del(): Option[Transposition] = {
    transpositions.remove(hash)
  }

  def get(): Option[Transposition] = {
    transpositions.get(hash)
  }
}

trait TranspositionTable2dArrayString extends TranspositionTable with Board2dArray {
  type T = String

  override def hash: String = board.flatten.mkString
}

trait TranspositionTable1dArrayString extends TranspositionTable with Board1dArray {
  type T = String

  override def hash: String = board.mkString
}

trait TranspositionTableBitInt extends TranspositionTable with BoardBitBoard {
  assert(mn <= 15, "BitBoardTransposition cannot work with boardMN s.t m*n > 15")
  assert(numPlayers <= 2, "cannot work for more than 2 players at the moment")
  type T = BitBoard

  // cannot be flatten, but can be shifted of m*n moves in a Int (if it is max mn=16)
  // at the moment would be ok....
  override def hash: BitBoard = (board(1) << mn) + board(0)
}