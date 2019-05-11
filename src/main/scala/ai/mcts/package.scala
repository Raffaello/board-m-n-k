package ai

import ai.mcts.tree.{Node, Tree}

package object mcts {
  final private val uctParameter: Double = Math.sqrt(2.0)

  /**
    * if visited is non zero then parentVisited is non zero
    */
  def UCT(score: Double, visited: Int, parentVisited: Int): Double = {
    require(visited >= 0)
    require(parentVisited > 0)

    visited match {
      case 0 => Double.MaxValue
      case _ => score / visited.toDouble + uctParameter * Math.sqrt(Math.log(parentVisited) / visited.toDouble)
    }
  }

  def selection(node: Node): Node = node.descending()

  def expansion(node: Node): Node = {
    node.expandChildren()
    node.randomChild()
  }

  /**
    * @TODO REDESIGN THIS METHOD !!!!!!
    */
  def simulation(node: Node, player: Byte): Double = {
    if (node.state.player == player) {
      val tempNode = node.copy()
      val tempState = tempNode.state.copy()
      // TODO LookUps nested object issue. (???)
      val tempBoard = tempState.board.clone()

      var opponent = tempBoard.opponent(player)
      var iter = 0
      val maxIter = 100
      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent) && iter < maxIter) {
        opponent = tempBoard.opponent(opponent)
        iter += 1
      }

      (tempBoard.score() + 1.0) / 2.0
    } else (node.state.board.score() + 1.0) / 2.0
  }

  def backPropagation(node: Node, player: Byte, gameScore: Double): Node = node.backPropagate(player, gameScore)

  /**
    * TODO REDO IT
    */
  def findNextMoveTest(game: MctsBoard, root: Node): Node = {
//    val root = game.root().root
    val player = root.state.player
    var process = true
    var bestNode: Node = root
    //    // TODO refactor Stats
    var totalCalls = 0
    while (process) {
      val selNode = selection(root)
      assert(selNode.children.isEmpty)
      // TODO use max time/iterations too
      // TODO review gameEnded method
      if (!selNode.state.board.gameEnded()) {
        val exploringNode = expansion(selNode)
        val gameScore = simulation(exploringNode, player)

        println(
          s"Simulation = score: $gameScore "
            + s"--- gameEnded: ${exploringNode.state.board.gameEnded()} "
            + s"-- depth: ${exploringNode.state.board.depth()}"
        )

        val tempRoot = backPropagation(exploringNode, player, gameScore)
        assert(tempRoot == root)
        totalCalls += 1
      } else {
        bestNode = selNode
        process = false
      }
    }

    println("Simulated game: ")
    bestNode.state.board.display()

    val bestRoot = bestNode.parentAscending()
    println("next move:")
    bestRoot.state.board.display()
    assert(bestRoot.parent.get == root)

    println(s"Total Calls: $totalCalls")
    //TODO UPDATE the tree root with best Node
    bestRoot
  }

  /**
    * TODO REDO IT
    */
  def solveTest(game: MctsBoard) = {
    val player: Byte = 2
    val tree = Tree(game, player)
    val root = tree.root
    findNextMoveTest(game, root)
  }
}
