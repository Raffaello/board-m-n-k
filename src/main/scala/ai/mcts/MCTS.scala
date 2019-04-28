package ai.mcts

import game.BoardMNK

object MCTS {
  def selection(root: Node): Node = {
    var node = root
    while (node.children.nonEmpty) {
      node = UCT.findBestNode(node)
    }

    node
  }

  def expansion(node: Node) = {
    val states = node.state.allPossibleStates()
    states.foreach(s => {
      val newNode = new Node()
      newNode.state = s
      newNode.parent = node
      newNode.state.player = node.state.opponent()
      node.children.append(newNode)
    })
  }

  def simulation(node: Node, player: Byte): Byte = {
    val tempNode = new Node()
    val tempState = tempNode.state
    val tempBoard = tempState.board
    var boardStatus = tempBoard.lastPlayer == player
    if (boardStatus) {
      tempNode.parent.state.score = Double.MinValue
      player // opponent?
    } else {
      // here play the game like minimax...
      while (!tempState.board.gameEnded()) {
        tempState.player = tempState.opponent()
        boardStatus = tempState.randomMove() && !tempBoard.gameEnded()
      }

      tempState.board.lastPlayer
    }

  }

  def backpropagation(node: Node, player: Short) = {
    var tempNode = node
    while (tempNode != null) {
      tempNode.state.visitCount += 1
      if (tempNode.state.player == player) {
        tempNode.state.score += tempNode.state.board.score()
      }

      tempNode = tempNode.parent
    }
  }

  def findNxetMove(game: BoardMNK, player: Byte) = {
    val opponent: Byte = (3 - player).toByte
    val root = new Node()
    root.state.board = game
    root.state.player = opponent
    val process=true
    while(end) {
      val tempNode = selection(root)
      if(!tempNode.state.board.gameEnded()) {
        expansion(tempNode)
      }

      val exploringNode = tempNode.randomChild()

      simulation(exploringNode, player)
      backpropagation(exploringNode, player)
    }

  }
}
