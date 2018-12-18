package ai

import game.{BoardMN, Position}

import scala.collection.immutable.NumericRange

private[ai] trait AiBoard extends BoardMN {

  //refactor later
  object Stats {
    var totalCalls: Int = 0
    var chacheHits: Int = 0
  }

  protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- NumericRange[Short](0, m, 1)
      j <- NumericRange[Short](0, n, 1)
      if board(i)(j) == 0
    } yield (i,j)
  }

  protected def consumeMoves()(f: Position => Unit): Unit = {
    generateMoves().foreach(f)
  }
}
