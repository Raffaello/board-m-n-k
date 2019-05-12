package ai.mcts

/**
  * TODO (probably) have MctsBoard is not memory efficient...
  * @TODO remove case class... not really useful...
  */
sealed case class State(board: MctsBoard, player: Byte, private var _visitCount: Int, private var _score: Double) {
  override def toString: String = s"W:${score()}, V:${visitCount()}"

  private[mcts] def zeroScore(): Unit = _score = 0.0

  private[mcts] def incVisitCount(): Unit = _visitCount += 1

  def visitCount(): Int = _visitCount

  def score(): Double = _score

  private[mcts] def addScore(deltaScore: Double): Unit = _score += deltaScore

  private[mcts] def allPossibleStates(): IndexedSeq[State] = {
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