package ai.mcts.tree

import ai.mcts.{MctsBoard, State, UCT, logger}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Random


final class Node(val state: State, private[mcts] var parent: Option[Node], val children: Children) {

//  def isLeaf(): Boolean = children.isEmpty
  def isTerminalLeaf(): Boolean = state.end() && children.isEmpty
//  def nonTerminalLeaf(): Boolean = children.isEmpty && !state.end()
  def isLeaf(): Boolean = children.isEmpty && !state.end()

  def copy(state: State = state, parent: Option[Node] = parent, children: Children = children): Node = {
    new Node(state, parent, children)
  }

  def addChild(child: Node): Unit = children.append(child)

  def addChild(state: State): Unit = addChild(Node(state, Some(this)))

  def addChildren(states: IndexedSeq[State]): Unit = states.foreach(addChild)

  def expandChildren(): Unit = addChildren(state.allPossibleStates())

  def randomChild(): Node = {
    if (children.isEmpty) this
    else children(Random.nextInt(children.length))
  }

  def mostVisited(): Node = {
    if (children.nonEmpty) children.maxBy(c => c.state.visitCount())
    else this
  }

  @tailrec
  def mostVisitedChild(): Node = {
    if (children.nonEmpty) children.maxBy(c => c.state.visitCount()).mostVisitedChild()
    else this
  }

  def Uct(): Double = {
    parent match {
      case None => Double.MaxValue
      case Some(p) => UCT(state.score(), state.visitCount(), p.state.visitCount())
    }

  }
  private def bestChildScore(): Node = {
    // debug
//    val l = new ListBuffer[Double]()
//
//    var bc:Node = null
//    var bu:Double = Double.MinValue
//    for (c <- children) {
//      val u = UCT(c.state.score(), c.state.visitCount(), state.visitCount())
//      logger.debug(s"uct = $u => [W:${c.state.score()}, V:${c.state.visitCount()}, N:${state.visitCount()}]")
//      l += u
//      if(u>bu) {
//        bu = u
//        bc = c
//      }
//    }
//
//    logger.debug(s"BC: $bc - uct = $bu")
//    logger.debug("---")
//    bc
    children.maxBy(c => UCT(c.state.score(), c.state.visitCount(), state.visitCount()))
  }

  // TODO: improve this method after the first expansion, there are a lot of Double.Max UCT children and
  // TODO: one of those should be chosen randomly... In this case is sequential always:
  // TODO: it would be always the first.
  // TODO: consider a priorityQueue as a cache too (benchmark).
  def bestChild(): Node = {
    // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
    if (children.nonEmpty) bestChildScore()
    else this
  }

  @tailrec
  def descending(): Node = {
    if (children.isEmpty) this
    else bestChildScore().descending()
  }

  @tailrec
  def ascending(): Node = {
    parent match {
      case None => this
      case Some(x) => x.ascending()
    }
  }

  /**
    * Improve performances
    * ALIAS: root.bestChild  => root.ascending.bestChild
    */
  def parentAscending(): Node = {
    @tailrec
    def loop(node: Node, last: Node): Node = {
      node.parent match {
        case Some(x) => loop(x, node)
        case None => last
      }
    }

    loop(this, this)
  }

  @tailrec
  def backPropagate(player: Byte, deltaScore: Double): Node = {
    state.incVisitCount()
    if (state.player == player) state.addScore(deltaScore)

    parent match {
      case None => this
      case Some(x) => x.backPropagate(player, deltaScore)
    }
  }
}

object Node {
  def apply(state: State): Node = Node(state.copy(), None)

  def apply(state: State, parent: Option[Node]) = new Node(state.copy(), parent, new Children())

  def apply(board: MctsBoard, player: Byte): Node = apply(State(board, player))
}