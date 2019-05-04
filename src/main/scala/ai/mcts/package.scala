package ai

import ai.mcts.tree.Node
import game.Board

package object mcts {
  final private val uctParameter: Double = Math.sqrt(2.0)

  sealed case class State(board: Board, player: Byte, visitCount: Int, score: Double) {
    def allPossibleStates() = {

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
    //    val states = node.state.allPossibleStates()
    //    states.foreach(s => {
    //      val newNode = node.copy(parent = Some(node), player = )
    //      newNode.state = s
    //      newNode.parent = node
    //      newNode.state.player = node.state.opponent()
    //      node.children.append(newNode)
    //    })

    val state = node.state
//    state.
    node.randomChild()
  }


  def simulation() = ???

  def backpropagation() = ???
}
