package ai.mcts.tree

import ai.mcts.{MctsBoard, State, uct}
import cats.implicits._

import scala.annotation.tailrec

protected[mcts] final class Node(val state: State, private[mcts] var parent: Option[Node], val children: Children) {

  def isLeaf: Boolean = children.isEmpty

  def isTerminal: Boolean = state.board.gameEnded()

  def isTerminalLeaf: Boolean = isTerminal && isLeaf

  def nonTerminalLeaf: Boolean = !isTerminal && isLeaf

  def copy(state: State = state, parent: Option[Node] = parent, children: Children = children): Node = {
    new Node(state, parent, children)
  }

  def deepCopy(state: State = state, parent: Option[Node] = parent, children: Children = children): Node = {
    val tempState = this.state.copy(board = this.state.board.clone())
    this.copy(state = tempState)
  }

  def addChild(child: Node): Unit = children.append(child)

  def addChild(state: State): Unit = addChild(Node(state, Some(this)))

  def addChildren(states: IndexedSeq[State]): Unit = states.foreach(addChild)

  def expandChildren(): Unit = addChildren(state.allPossibleStates())

  def randomChild(): Node = {
    if (children.isEmpty) this
    else children(this.state.board.random.nextInt(children.length))
  }

  private[this] def mostVisitedChild(): Option[Node] = {
    if (children.isEmpty) None
    else Some(children.maxBy(c => c.state.visitCount()))
  }

  def mostVisited(): Node = {
    mostVisitedChild() match {
      case None => this
      case Some(x) => x
    }
  }

  @tailrec
  def mostVisitedDescending(): Node = {
    mostVisitedChild() match {
      case None => this
      case Some(x) => x.mostVisitedDescending()
    }
  }

  private[this] def bestChildScore(): Node =
    children.maxBy(c => uct(c.state.score, c.state.visitCount(), state.visitCount()))


  // TODO: improve this method after the first expansion, there are a lot of Double.Max UCT children and
  // TODO: one of those should be chosen randomly... In this case is sequential always:
  // TODO: it would be always the first.
  // TODO: consider a priorityQueue as a cache too (benchmark).
  def bestChild(): Node = {
    // TODO: UCT can be cached in the node and invalidated/updated in back-propagation.
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
    if (state.player === player) state.addScore(deltaScore)

    parent match {
      case None => this
      case Some(x) => x.backPropagate(player, deltaScore)
    }
  }
}

object Node {
  def apply(state: State): Node = Node(state.copy(), None)

  def apply(state: State, parent: Option[Node]): Node = new Node(state.copy(), parent, new Children())

  def apply(board: MctsBoard, player: Byte): Node = apply(State(board, player))
}
