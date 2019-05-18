package ai

import ai.mcts.tree._
import cats.implicits._
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import game.Score

import scala.annotation.tailrec
import scala.util.Random

package object mcts {
  private[mcts] val logger = Logger("mcts")

  val config: Config = ai.config.getConfig("mcts")

  final private[this] val uctParameter: Double = config.getDouble("uct.c")
  final private[this] val maxIter: Int = config.getInt("max_iter")

  final val seed: Option[Long] =  if (config.getIsNull("seed")) None else Some(config.getLong("seed"))

  protected def remapScore(score: Score, player: Byte): Double = {
    score match {
      case -1 => if (player === 2) 1.0 else 0.0
      case 1 => if (player === 1) 1.0 else 0.0
      case 0 => 0.5
      case _ => ???
    }
  }

  protected def remapScore(board: MctsBoard, player: Byte): Double = remapScore(board.score(), player)

  /**
    * if visited is non zero then parentVisited is non zero
    */
  def uct(score: Double, visited: Int, parentVisited: Int): Double = {
    require(visited >= 0)
    require(parentVisited > 0)

    visited match {
      case 0 => Double.MaxValue
      case _ => score / visited.toDouble + Math.sqrt(uctParameter * Math.log(parentVisited) / visited.toDouble)
    }
  }

  def selection(node: tree.Node): tree.Node = node.descending()

  def expansion(node: Node): Node = {
    if (node.nonTerminalLeaf) node.expandChildren()
    node.randomChild()
  }

  def simulation(node: Node): Double = {
    @tailrec
    def gameLoop(board: MctsBoard, player: Byte): MctsBoard = {
      if (!board.gameEnded() && board.playRandomMove(player)) gameLoop(board, board.opponent(player))
      else board
    }

    if (node.nonTerminalLeaf) {
      val tempNode = node.deepCopy()
      val loopBoard = gameLoop(tempNode.state.board, tempNode.state.opponent())
      remapScore(loopBoard, node.state.player)
    } else remapScore(node.state.board, node.state.player)
  }

  def backPropagation(node: Node, gameScore: Double): Node = node.backPropagate(node.state.player, gameScore)

  def playNextMove(tree: Tree): Option[Tree] = {
    if (tree.root.isTerminal) None
    else {
      val newRoot = findNextMove(tree.root)
      val newTree = Tree.from(newRoot)
      logger.info(s"lastMove = ${newTree.root.state.board.lastMove} -- player = ${newTree.root.state.player}")
      Some(newTree)
    }
  }

  def findNextMove(root: Node): Node = {
    @tailrec
    def loop(iter: Int = 0): Int = {
      if (iter < maxIter) {
        val selNode = selection(root)
        val expNode = expansion(selNode)
        val gameScore = simulation(expNode)
        backPropagation(expNode, gameScore)
        loop(iter + 1)
      } else iter
    }

    val iter = loop()

    val bestRoot = root.mostVisited()
    val bestNode = bestRoot.mostVisitedDescending()

    logger.debug(
      s"""Simulated game:
         |${bestNode.state.board.display()}
         |next move:
         |${bestRoot.state.board.display()}
         |Total Calls (iter): $iter
       """.stripMargin)

    bestRoot
  }
}
