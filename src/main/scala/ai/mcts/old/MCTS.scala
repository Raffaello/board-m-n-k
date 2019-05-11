package ai.mcts.old

import scala.annotation.tailrec

/**
  * @deprecated
  */
object MCTS {

  def solve(game: BoardTicTacToeMcts) = {

    val root = new Node()
    val rootState = new BoardState(game.m, game.n)
    rootState.player = 1
    root.state = rootState
    root.parent = null

    findNextMove(game, root)
  }

  def selection(root: Node): Node = {
    @tailrec
    def descending(node: Node): Node = {
      if (node.children.isEmpty) node
      else descending(UCT.findBestNode(node))
    }

    descending(root)
  }

  /**
    * TODO expand only the one needed ???
    * @param node
    */
  def expansion(node: Node) = {
    assert(node.children.isEmpty)
    val states = node.state.allPossibleStates()
    states.foreach(s => {
      val newNode = new Node()
      newNode.state = s
      newNode.parent = node
      newNode.state.player = node.state.opponent()
      node.children.append(newNode)
    })
  }

  def simulation(node: Node, player: Byte): Node = {
    val tempNode = new Node()
    // TODO create a clone method
    tempNode.parent = node.parent
    tempNode.children = node.children
    tempNode.state = new BoardState(node.state.m, node.state.n)
    val tempState = tempNode.state
    tempState.clone(node.state)

    if (tempState.player == player) {
      // here play the game...
      // TODO: inefficient using gameEnded... review it
      // TODO: add number of max time / iterations
      while (!tempState.gameEnded() && tempState.randomMove()) {
        tempState.player = tempState.opponent()
      }
    }

    node.state.stateScore += tempState.score()
    tempNode // ???
  }

  def backpropagation(node: Node, player: Short): Unit = {
    var tempNode = node
    while (tempNode != null) {
      tempNode.state.visitCount += 1
      if (tempNode.state.player == player) {
        tempNode.state.stateScore += node.state.stateScore
      }

      tempNode = tempNode.parent
    }
  }

  def findNextMove(game: BoardTicTacToeMcts, root: Node): Unit = {
    val player = root.state.player
    var process = true
    var bestNode: Node = root
    // TODO refactor Stats
    var totalCalls = 0
    while (process) {
      val tempNode = selection(root)
      assert(tempNode.children.isEmpty)
      // TODO use max time/iterations too
      // TODO review gameEnded method
      if (!tempNode.state.gameEnded()) {

        expansion(tempNode)
        val exploringNode = tempNode.randomChild()
        val simulatedNode = simulation(exploringNode, player)

        println(
          s"Simulation = score: ${simulatedNode.state.stateScore} --- visited = ${simulatedNode.state.visitCount} "
            + s"--- selNode: ${tempNode.state.getBoard().flatten.mkString} --- expMode: ${exploringNode.state.getBoard().flatten.mkString} --- gameEnded: ${exploringNode.state.gameEnded()} "
            + s"-- depth: ${exploringNode.state.depth()}"
        )

        backpropagation(exploringNode, player)
        totalCalls += 1
      } else {
        bestNode = tempNode
        process = false
      }
    }

    println("Simulated game: ")
    bestNode.state.stdoutPrintln()
    // TODO refactor in node to get root
    while (bestNode.parent != root) {
      bestNode = bestNode.parent
    }

    println("next move:")
    bestNode.state.stdoutPrintln()

    println(s"Total Calls: $totalCalls")
  }
}
