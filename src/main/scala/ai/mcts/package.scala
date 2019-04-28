package ai

import game.BoardMNK

import scala.collection.mutable.ArrayBuffer

package object mcts {

  object UCT {
    val c: Double = Math.sqrt(2.0)
    def UCT(w: Double, n: Int, t: Int): Double = {
      n match {
        case 0 => Double.MaxValue
        case _ => w / n.toDouble + c * Math.sqrt(Math.log(t) / n.toDouble)
      }
    }

    def findBestNode(node: Node): Node = {
      node.children.maxBy( c => UCT(c.state.score, node.state.visitCount, c.state.visitCount ))
    }
  }

  class Node {
    var state: State = _
    var parent: Node = null
    var children: ArrayBuffer[Node] = new ArrayBuffer[Node]()

    //    def UCT(): Double = w.toDouble / n.toDouble + c * Math.sqrt(Math.log(t) / n.toDouble)
    def randomChild(): Node = {
      val size = children.length
      size match {
        case 0 => this
        case _ => children(Math.round(Math.random() * children.length).toInt)
      }
    }
  }

  class State {
    var board: BoardMNK = _
    var player: Byte = _
    var visitCount: Int = 0
    var score: Double = Double.MinValue

    def allPossibleStates(): List[State] = {
      Nil
    }

    def randomMove(): Boolean = {
      false
    }

    def randomPlay(): Boolean = false

    // assuming player 1 or 2
    def opponent(): Byte = (3 - player).toByte
  }
}
