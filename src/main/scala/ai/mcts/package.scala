package ai

import ai.mcts.tree.{Node, Tree}
import com.typesafe.scalalogging.Logger
import game.Score

package object mcts {
  private[mcts] val logger = Logger("mcts")

  // TODO import from config
  final private val uctParameter: Double = Math.sqrt(2.0)
  final private val maxIter: Int = 1000

  protected def remapScore(score: Score, player: Byte): Double = {
    score match {
      case -1 => if (player == 2) 1.0 else 0.0
      case 1 => if (player == 1) 1.0 else 0.0
      case 0 => 0.5
      case _ => ???
    }
  }

  protected def remapScore(board: MctsBoard, player: Byte): Double = remapScore(board.score(), player)

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
    if (node.nonTerminalLeaf()) node.expandChildren()
    node.randomChild()
  }

  def simulation(node: Node): Double = {
    if (node.nonTerminalLeaf()) {
      val tempNode = node.copy()
      val tempState = tempNode.state.copy()
      val tempBoard = tempState.board.clone()

      var opponent = tempState.opponent()
      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent)) {
        opponent = tempBoard.opponent(opponent)
      }

      remapScore(tempBoard, node.state.player)
    } else remapScore(node.state.board, node.state.player)
  }

  def backPropagation(node: Node, gameScore: Double): Node = node.backPropagate(node.state.player, gameScore)

  def playNextMove(tree: Tree): Option[Tree] = {
    if (tree.root.isTerminal()) None
    else {
      val newRoot = findNextMove(tree.root)
      val newTree = Tree.from(newRoot)
      logger.info(s"lastMove = ${newTree.root.state.board.lastMove()} -- player = ${newTree.root.state.player}")
      Some(newTree)
    }
  }

  def findNextMove(root: Node): Node = {
    // TODO fix game.Stats object.... remove singleton.
    //    game.Stats.totalCalls = 0
    var iter = 0
    while (iter < maxIter) {
      val selNode = selection(root)
      val expNode = expansion(selNode)
      val gameScore = simulation(expNode)
      backPropagation(expNode, gameScore)
      iter += 1
    }

    val bestRoot = root.mostVisited()
    val bestNode = bestRoot.mostVisitedDescending()

    logger.debug(
      s"""Simulated game ${bestNode.state.board.boardStatus()}:
         |${bestNode.state.board.display()}
         |next move:
         |${bestRoot.state.board.display()}
         |Total Calls (iter): $iter
       """.stripMargin)

    bestRoot
  }
}
