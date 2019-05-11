package ai

import ai.mcts.tree.{Node, Tree}
import com.typesafe.scalalogging.Logger
import game.Score

package object mcts {
  private[mcts] val logger = Logger("mcts")

  // TODO import from config
  final private val uctParameter: Double = Math.sqrt(2.0)
  final private val maxIter: Int = 10

  protected def remapScore(score: Score): Double = (score + 1.0) / 2.0
  protected def remapScore(board: MctsBoard): Double = remapScore(board.score())

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

  def simulation(node: Node, player: Byte): Double = {
    if (node.state.player == player) {
      // TODO can use Lens for deep copy/clone {for these 3 statements}
      val tempNode = node.copy()
      val tempState = tempNode.state.copy()
      val tempBoard = tempState.board.clone()

      var opponent = tempBoard.opponent(player)
      var iter = 0
      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent) && iter < maxIter) {
        opponent = tempBoard.opponent(opponent)
        iter += 1
      }

      remapScore(tempBoard)
    } else remapScore(node.state.board)
  }

  def backPropagation(node: Node, player: Byte, gameScore: Double): Node = node.backPropagate(player, gameScore)

  def findNextMove(tree: Tree): Tree = {
    val newRoot = findNextMove(tree.root)

    Tree(newRoot)
  }

  def playNextMove(tree: Tree): Tree = {
    val newTree = Tree.update(findNextMove(tree.root))
    // todo if not true?
    logger.error(s"lastMove = ${newTree.root.state.board.lastMove()}")
    newTree
  }

  def findNextMove(root: Node): Node = {
    val player = root.state.player
    var process = true
    var bestNode: Node = root
    val game = root.state.board
    game.Stats.totalCalls = 0
    while (process) {
      val selNode = selection(root)
      assert(selNode.children.isEmpty)
      // TODO use max time/iterations too
      // TODO review gameEnded method
      if (!selNode.state.board.gameEnded()) {
        val exploringNode = expansion(selNode)
        val gameScore = simulation(exploringNode, player)
        logger.debug(
            s"Simulation = score: $gameScore "
              + s"--- gameEnded: ${exploringNode.state.board.gameEnded()} "
              + s"-- depth: ${exploringNode.state.board.depth()}"
          )

        val tempRoot = backPropagation(exploringNode, player, gameScore)
        assert(tempRoot == root)
        game.Stats.totalCalls += 1
      } else {
        bestNode = selNode
        process = false
      }
    }

    val bestRoot = bestNode.parentAscending()
//    assert(bestRoot.parent.get == root)

    // TODO display flag instead?
    logger.whenDebugEnabled {
      logger.info(
        s"""Simulated game:
           |${bestNode.state.board.display()}
           |next move:
           |${bestRoot.state.board.display()}
           |Total Calls: ${game.Stats.totalCalls}
         """.stripMargin)
    }

    bestRoot
  }

  /**
    * TODO REDO IT
    */
  def solveTest(game: MctsBoard) = {
    val player: Byte = 2
    val tree = Tree(game, player)
    val newTree = findNextMove(tree)

    assert(tree ne newTree)
    assert(newTree.root.state.board.depth() == 1)
  }
}
