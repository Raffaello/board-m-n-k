package ai.mcts

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Random

package object tree {

  // TODO this class is the most important one
  // TODO design a proper type and "clone" "copy" efficient method

  // TODO Instead of Children consider to use a PriorityQueue for have ready the BestNode for UCT. ???
  // TODO Children is used for descending in selection phase.

  /**
    * TODO the parent must be null to free memory. ???
    */
  case class Tree(root: Node) {
        // NOT USED. ???
  }

  /**
    * TODO refactor with a type class
    */
  final case class Node(state: State, parent: Option[Node], children: ListBuffer[Node]) {
    def randomChild(): Node = {
      if (children.isEmpty) this
      else children(Random.nextInt(children.length))
    }

    def bestChildren(): Node = {
      // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
      children.maxBy(c => UCT(c.state.score, c.state.visitCount, state.visitCount))
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
