package ai.mcts

import ai.AiBoard

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Random

package object tree {

  type Children = ListBuffer[Node]

  // TODO this class is the most important one
  // TODO design a proper type and "clone" "copy" efficient method

  // TODO Instead of Children consider to use a PriorityQueue for have ready the BestNode for UCT. ???
  // TODO Children is used for descending in selection phase.

  // ---------------------------------------------------------------------------------------------------------------

  final case class Tree(root: Node) {}

  object Tree {
    def apply(game: AiBoard, player: Byte): Tree = {
      val state = State(game, player, 0, Double.MinValue)
      val root = Node(state, None, new Children())
      new Tree(root)
    }

    def update(newRoot: Node): Tree = {
      val rootClone = newRoot.copy(parent = None)
      new Tree(rootClone)
    }
  }

  /**
    * TODO refactor with a type class?
    * TODO use a children: Option[ListBuffer[Node]] and nonEmptyList ?
    *
    * TODO not required to be a case class.. could be just a class at the moment
    */
  final case class Node(state: State, parent: Option[Node], children: Children) {
    def addChild(child: Node): Unit = children.append(child)

    def addChild(state: State): Unit = addChild(Node(state, Some(this), new Children))

    def addChildren(states: IndexedSeq[State]): Unit = states.foreach(addChild)

    def expandChildren(): Unit = addChildren(state.allPossibleStates())

    def randomChild(): Node = {
      if (children.isEmpty) this
      else children(Random.nextInt(children.length))
    }

    def bestChildren(): Node = {
      // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
      if (children.nonEmpty) children.maxBy(c => UCT(c.state.score, c.state.visitCount, state.visitCount))
      else this
    }

    @tailrec
    def descending(): Node = {
      if (children.isEmpty) this
      else bestChildren().descending()
    }

    @tailrec
    def ascending(): Node = {
      parent match {
        case None => this
        case Some(x) => x.ascending()
      }
    }

    @tailrec
    def backPropagate(player: Byte, deltaScore: Double): Node = {
      state.visitCount += 1
      if (state.player == player) state.score += deltaScore

      parent match {
        case None => this
        case Some(x) => x.backPropagate(player, deltaScore)
      }
    }
  }

}
