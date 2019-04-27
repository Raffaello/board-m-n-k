package ai

import game.{BoardMNKP, Position}

/*private[ai]*/ trait AiBoard extends BoardMNKP {

  //refactor later
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board((i, j)) == 0
    } yield (i,j)
  }

  protected def consumeMoves()(f: Position => Unit): Unit = {
    generateMoves().foreach(f)
  }
}
