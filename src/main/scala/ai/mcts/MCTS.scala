package ai.mcts

import ai.AiBoard
import game.{BoardMNK, BoardTicTacToe}

object MCTS {

  def solve(game: BoardTicTacToe) = {

    val root = new Node()
    val rootState = new BoardState(game.m, game.n)
    //rootState.clone(game)
//    rootState.board = game.board
    rootState.player = 1 // it is not player 1, so should be player 2 (because 1st move is for p1)
    root.state = rootState
    root.parent = null

//    val selectedNode = selection(root)
//    val expandedNode = expansion(selectedNode)
//    val simulatedNode = simulation(expandedNode, expandedNode)

//    MCTS.simulation(root, 1)
    findNextMove(game, root)
  }

  def selection(root: Node): Node = {
    var node = root
    while (node.children.nonEmpty) {
      node = UCT.findBestNode(node)
    }

    node
  }

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
//    tempNode.parent = node
    tempNode.parent = node.parent
    tempNode.children = node.children
//    tempNode.state
    tempNode.state = new BoardState(node.state.m, node.state.n)
    val tempState = tempNode.state
    tempState.clone(node.state)

    var boardStatus = tempState.player == player
    if (!boardStatus) {
//      tempNode.parent.state.stateScore = Double.MinValue
//      tempState.player // opponent?
    } else {
      // here play the game...
      while (!tempState.gameEnded() && boardStatus) {
        boardStatus = tempState.randomMove()
        tempState.player = tempState.opponent()
      }
    }

//    val stateScore = (tempState.score()+1.0) / 2.0
//    node.state.stateScore += stateScore
    node.state.stateScore += tempState.score()

    tempNode
  }

  def backpropagation(node: Node, player: Short) = {
    var tempNode = node
    while (tempNode != null) {
      tempNode.state.visitCount += 1
      if (tempNode.state.player == player) {
        tempNode.state.stateScore += node.state.stateScore
        assert(!tempNode.state.stateScore.isInfinity)
      }

      tempNode = tempNode.parent
    }
  }

  def findNextMove(game: BoardTicTacToe, root: Node) = {
    val player = root.state.player
//    val opponent: Byte = root.state.opponent()
    var process=true
    var bestNode:Node = root
    while(process) {
      val tempNode = selection(root)
      assert(tempNode.children.isEmpty)
      if(!tempNode.state.gameEnded()) {
        expansion(tempNode)


        val exploringNode = tempNode.randomChild()

        val simulatedNode = simulation(exploringNode, player)
        println(
          s"Simulation = score: ${simulatedNode.state.stateScore} --- visited = ${simulatedNode.state.visitCount} "
          + s"--- selNode: ${tempNode} --- expMode: ${exploringNode} --- gameEnded: ${exploringNode.state.gameEnded()} "
          + s"-- depth: ${exploringNode.state.depth}"
        )
        backpropagation(exploringNode, player)
      } else {
        bestNode = tempNode
        process=false
      }
    }
    println("Simulated game: ")
    bestNode.state.display()
    while(bestNode.parent != root) {
      bestNode = bestNode.parent
    }

    println("next move:")
    bestNode.state.display()
//    bestNode.state.
  }
}
