package game.boards.concrete

import cats.implicits._
import game.Player
import game.boards.{BoardMovesGenerator, BoardT}
import game.types.{BoardMNType2dArray, Position}

// TODO: decouple the 2 used traits if it is possible. Type Classes?
trait Board2dArray extends BoardMNType2dArray with BoardT with BoardMovesGenerator {
  @inline
  protected def boardPlayer(pos: Position): Player = board(pos.row)(pos.col)

  @inline
  protected def boardPlayer_=(pos: Position)(p: Player): Unit = board(pos.row)(pos.col) = p

  def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if boardPlayer(Position(i, j)) === 0
    } yield Position(i, j)
  }
}
