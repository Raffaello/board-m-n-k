package ai

import ai.mcts.tree.Node
import game.Board

package object mcts {
  final private val uctParameter: Double = Math.sqrt(2.0)

  sealed case class State(board: Board, player: Byte, visitCount: Int, score: Double) /*extends AiBoardState(board, player)*/ {
    def allPossibleStates(aiBoard: AiBoard): IndexedSeq[State] = {
      // TODO with board array doesn't work with Lookups... :/
      // TODO if pass AiBoard Trait instead of Board type.... ???
      val opponent = aiBoard.opponent(player)
      val allStates = aiBoard.allPossibleMoves()
      val states = Array.ofDim[State](allStates.length)
      for (i <- allStates.indices) {
        val (x, y) = allStates(i)
        val newState = copy(player = opponent)
        newState.board(x)(y) = opponent
        states(i) = newState
      }

      states.toIndexedSeq
    }
  }

  def UCT(w: Double, n: Int, N: Int): Double = {
    n match {
      case 0 => Double.MaxValue
      case _ => w / n.toDouble + uctParameter * Math.sqrt(Math.log(N) / n.toDouble)
    }
  }

  def selection(node: Node): Node = node.descending()

  /**
    * TODO Not ok.. review and refactor, because require AiBoard
    */
  def expansion(node: Node)(aiBoard: AiBoard): Node = {
    assert(node.children.isEmpty)
    val states = node.state.allPossibleStates(aiBoard)
    states.foreach { s =>
      val newNode = node.copy(parent = Some(node), state = s)
      node.children.append(newNode)
    }

    node.randomChild()
  }


  def simulation(node: Node, player: Byte)(aiBoard: AiBoard): Double = {
    val tempNode = node.copy()
    val tempState = tempNode.state
    if(tempState.player == player) {
      // TODO without game rules in the game state, how can i simulate a match? :)
      // TODO redesign from this point where appropriated.
    }

    // return score??
    0
  }

  def backpropagation(node: Node, player: Byte, gameScore: Double) = {
    //assert(node.state.score == score) // TODO: why? It should not be the same always...
    node.backPropagate(player, gameScore)
    // assert(node.backpropagate == root)
  }
}
