package ai.mcts

// TODO review var visitConunt, score
// TODO could just be enough BoardMNKP ?
// TODO (probably) have AiBoard or BoardMNKP is not memory efficient...
// TODO with AiBoard still need the Board type clone ... NOT OK. !!!!!!!!!
sealed case class State(board: MctsBoard, player: Byte, var visitCount: Int, var score: Double) /*extends AiBoardState(board, player)*/ {
  def allPossibleStates(): IndexedSeq[State] = {
    val opponent = board.opponent(player)
    val allStates = board.allPossibleMoves()
    val states = Array.ofDim[State](allStates.length)
    for (i <- allStates.indices) {
      val pos = allStates(i)
      val newBoard: MctsBoard = board//.clone()
      val newState = copy(board=newBoard, player = opponent)
      newState.board.playMove(pos, opponent)
      states(i) = newState
    }

    states.toIndexedSeq
  }
}

object State {
  def apply(board: MctsBoard, player: Byte): State = new State(board, player, 0, 0.0)
//  def apply()
}