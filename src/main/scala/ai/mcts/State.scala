package ai.mcts

/**
  * TODO (probably) have MctsBoard is not memory efficient...
  */
sealed case class State(board: MctsBoard, player: Byte, private var _visitCount: Int, private var _score: Double) {

  def incVisitCount(): Unit = _visitCount += 1

  def visitCount(): Int = _visitCount

  def score(): Double = _score

  def addScore(deltaScore: Double): Unit = _score += deltaScore

  def allPossibleStates(): IndexedSeq[State] = {
    val opp = opponent()
    val allStates = board.allPossibleMoves()
    val states = Array.ofDim[State](allStates.length)
    for (i <- allStates.indices) {
      val pos = allStates(i)
      val newBoard: MctsBoard = board.clone()
      val newState = copy(board = newBoard, player = opp)
      newState.board.playMove(pos, opp)
      states(i) = newState
    }

    states.toIndexedSeq
  }

  def opponent(): Byte = board.opponent(player)
}

object State {
  def apply(board: MctsBoard, player: Byte): State = new State(board, player, 0, 0.0)
}