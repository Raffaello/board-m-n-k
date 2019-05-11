package ai.mcts.tree

import ai.mcts.{MctsBoard, State, UCT}

import scala.annotation.tailrec
import scala.util.Random

final class Node(val state: State, private[mcts] var parent: Option[Node], val children: Children) {

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

  // TODO: improve this method after the first expansion, there are a lot of Double.Max UCT children and
  // TODO: one of those should be chosen randomly... In this case is sequential always:
  // TODO: it would be always the first.
  // TODO: consider a priorityQueue as a cache too (benchmark).
  def bestChild(): Node = {
    // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
    if (children.nonEmpty) children.maxBy(c => UCT(c.state.score(), c.state.visitCount(), state.visitCount()))
    else this // not sure is correct, should be None
  }

  @tailrec
  def descending(): Node = {
    if (children.isEmpty) this
    else bestChild().descending()
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