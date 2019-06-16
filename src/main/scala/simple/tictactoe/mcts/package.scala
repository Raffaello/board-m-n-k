package simple.tictactoe

import types.Position

import scala.util.Random

package object mcts {
  // Implementation of a basic MCTS Tic-Tac-Toe game
  Random.setSeed(0)

  def mctsMove(rootState: GameState, itermax: Int, verbose: Boolean): Option[Position] = {
    val rootNode = new Node(None, None, rootState)

    for (_ <- 0 until itermax) {
      var node = rootNode
      val state = rootState.copy()
      val player = node.state.currentPlayer()

      // select
      while (node.untriedMoves.isEmpty && node.children.nonEmpty) {
        node = node.UCTSelectChild()
        state.playMove(node.move.get)
      }

      // expand
      if (node.untriedMoves.nonEmpty) {
        val indexes = node.untriedMoves.indices
        val randomIndex = Random.nextInt(indexes.length)
        val m = node.untriedMoves(randomIndex)

        state.playMove(m)
        node = node.addChild(Some(m), state)
      }

      // simulation
      while (state.allRemainingMoves().nonEmpty && state.result(state.currentPlayer()) == 0.5) {
        val moves = state.allRemainingMoves()
        val indexes = moves.indices
        val randomIndex = Random.nextInt(indexes.length)
        state.playMove(moves(randomIndex))
      }

      // backpropagation
      while (node != null) {
        val score = state.result(node.lastPlayer)
        node.update(score)
        node = node.parent.orNull
      }
    }

    if (verbose) println(rootNode.treeToString(0))
    else println(rootNode.childrenToString())

    rootNode.children.maxBy(c => c.wins / c.visits.toDouble).move
  }
}
