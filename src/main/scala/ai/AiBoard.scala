package ai

import game.{BoardMNKPLookUp, Position}


//private[ai] abstract class AiBoardState(board: Board, player: Byte)

/**
  * TODO refactor and/or remove AiBoard Trait. ???
  */
private[ai] trait AiBoard extends BoardMNKPLookUp with AiStats {
  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()
}
