package cakes.ai.mcts.tree

import cakes.ai.mcts.{MctsBoard, State, uct}
import cats.implicits._

import scala.annotation.tailrec

protected[mcts] final case class Node(state: State, private[mcts] var parent: Option[Node], children: Children) {

  override def toString: String = {
    val uctStr = parent match {
      case None => "NONE"
      case Some(p) => uct(state.score, state.visitCount(), p.state.visitCount()).formatted("%.5f")
    }
    state.toString + " | " + uctStr
  }

  def isLeaf: Boolean = children.isEmpty

  def isTerminal: Boolean = state.board.gameEnded()

  def isTerminalLeaf: Boolean = isTerminal && isLeaf

  def nonTerminalLeaf: Boolean = !isTerminal && isLeaf

  def deepCopy(state: State = state, parent: Option[Node] = parent, children: Children = children): Node = {
    val tempState = this.state.copy(board = this.state.board.clone())
    copy(state = tempState)
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
      case None => ???
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
  def foldAsc(op: Node => Node): Node = {
    op(this)
    parent match {
      case None => this
      case Some(x) => x.foldAsc(op)
    }
  }

  def ascending(): Node = foldAsc(x => x)

  //  @tailrec
  //  def ascending(): Node = {
  //    parent match {
  //      case None => this
  //      case Some(x) => x.ascending()
  //    }
  //  }

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

//  def backPropagate(player: Byte, deltaScore: Double): Node = foldAsc { x =>
//    x.state.incVisitCount()
//    if (deltaScore === 0.5) x.state.addScore(deltaScore)
//    else if (player === x.state.player) x.state.addScore(deltaScore)
//    x
//  }

      @tailrec
      def backPropagate(player: Byte, deltaScore: Double): Node = {
        state.incVisitCount()
        state.addScore(deltaScore)
//        if (deltaScore === 0.5) state.addScore(deltaScore)
//        else if (state.player === player) state.addScore(deltaScore)
        parent match {
          case None => this
          case Some(x) => x.backPropagate(player, 1.0 - deltaScore)
        }
      }
}

object Node {
  def apply(state: State): Node = Node(state.copy(), None)

  def apply(state: State, parent: Option[Node]): Node = new Node(state.copy(), parent, new Children())

  def apply(board: MctsBoard, player: Byte): Node = apply(State(board, player))
}
