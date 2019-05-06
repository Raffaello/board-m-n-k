package ai

import game.{BoardMNKPLookUp, Position}


private[ai] trait AiBoardState {
//  def clone(): B = {
//
//  }
}

/**
  * TODO refactor and/or remove AiBoard Trait. ???
  */
private[ai] trait AiBoard extends BoardMNKPLookUp with AiStats {
  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def randomMove(player: Byte): Boolean = {
    val moves = generateMoves()
    if (moves.nonEmpty) {
      moves.lift(scala.util.Random.nextInt(moves.size)) match {
        case Some(pos) => playMove(pos, player)
        case None => false
      }
    } else false
  }
}
