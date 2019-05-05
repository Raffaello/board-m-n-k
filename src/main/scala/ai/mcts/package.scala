package ai

import ai.mcts.tree.Node
import game.Board

import scala.collection.mutable.ListBuffer

package object mcts {
  final private val uctParameter: Double = Math.sqrt(2.0)

  // TODO review var visitConunt, score
  // TODO could just be enough BoardMNKP ?
  // TODO (probably) have AiBoard or BoardMNKP is not memory efficient...
  // TODO with AiBoard still need the Board type clone ... NOT OK.
  sealed case class State(board: AiBoard, player: Byte, var visitCount: Int, var score: Double) /*extends AiBoardState(board, player)*/ {
    def allPossibleStates(): IndexedSeq[State] = {
      val opponent = board.opponent(player)
      val allStates = board.allPossibleMoves()
      val states = Array.ofDim[State](allStates.length)
      for (i <- allStates.indices) {
        val pos = allStates(i)
        val newState = copy(player = opponent)
//        newState.board(x)(y) = opponent
        newState.board.playMove(pos, opponent)
        states(i) = newState
      }

      states.toIndexedSeq
    }
  }

  def UCT(w: Double, n: Int, N: Int): Double = {
    n match {
      case 0 => Double.MaxValue
      case _ => w / n.toDouble + uctParameter * Math.sqrt(Math.log(N) / n.toDouble)
    }
  }

  def selection(node: Node): Node = node.descending()

  def expansion(node: Node): Node = {
    assert(node.children.isEmpty)
    val states = node.state.allPossibleStates()
    states.foreach { s =>
      // TODO case class are doing deep cloning is not working as expected in a Tree structure
      // TODO redo itz.
      val newNode = node.copy(parent = Some(node), state = s, children = new ListBuffer[Node])
//      val newNode = new Node(s, Some(node), new ListBuffer[Node])
      node.children.append(newNode)
    }

    node.randomChild()
  }

  def simulation(node: Node, player: Byte): Double = {
    if(node.state.player == player) {
      val tempNode = node.copy()
      val tempState = tempNode.state.copy()
      val tempBoard = tempState.board

      var opponent = tempBoard.opponent(player)
      var iter = 0
      val maxIter = 100
      while (!tempBoard.gameEnded() && tempBoard.randomMove(opponent) && iter < maxIter) {
        opponent = tempBoard.opponent(opponent)
        iter += 1
      }

      (tempBoard.score() + 1.0) / 2.0
    } else (node.state.board.score() + 1.0) / 2.0
  }

  def backpropagation(node: Node, player: Byte, gameScore: Double) = {
    //assert(node.state.score == score) // TODO: why? It should not be the same always...
    node.backPropagate(player, gameScore)
    // assert(node.backpropagate == root)
  }

  def findNextMoveTest(game: AiBoard, root: Node): Unit = {
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
            + s"-- depth: ${exploringNode.state.board.depth}"
        )

        val tempRoot = backpropagation(exploringNode, player, gameScore)
        assert(tempRoot == root)
        totalCalls += 1
      } else {
        bestNode = selNode
        process = false
      }
    }

    println("Simulated game: ")
    bestNode.state.board.display()

    val bestRoot = bestNode.ascending()
    println("next move:")
    bestRoot.state.board.display()
    assert(bestRoot == root)

    println(s"Total Calls: $totalCalls")
  }

  def solveTest(game: AiBoard) = {

    val root = new Node(State(game, 1, 0, Double.MinValue), None, new ListBuffer[Node])
    findNextMoveTest(game, root)
  }
}
