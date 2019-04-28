package ai

import game.{BoardMN, Position, Status}

/*private[ai]*/ trait AiBoard extends BoardMN with AiStats {
  type AB = (Int, Int) // Alpha, Beta values
  //  type ABScore = (AB, Int) // Alpha, Beta values plus score
  //  type ABStatus = (AB, Status) // Alpha, Beta values plus Status
  type ABStatus = (AB, Status) // Alpha, Beta values plus Status: Score, Position

  protected def generateMoves(): IndexedSeq[Position] = {
    for {
      i <- mIndices
      j <- nIndices
      if board(i)(j) == 0
    } yield (i,j)
  }

  protected def consumeMoves()(f: Position => Unit): Unit = {
    generateMoves().foreach(f)
  }
}
