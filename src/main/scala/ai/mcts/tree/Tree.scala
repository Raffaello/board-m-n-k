package ai.mcts.tree

import ai.mcts.{MctsBoard, State}
import game.Position

final class Tree(val root: Node) {
  def lastMove: Position = root.state.board.lastMove

  def lastPlayer: Byte = root.state.player
}

object Tree {
  def apply(game: MctsBoard, player: Byte): Tree = {
    val state = State(game.clone(), player)
    val root = Node(state, None)
    new Tree(root)
  }

  def apply(root: Node): Tree = {
    val newRoot = root.copy(parent = None)
    new Tree(newRoot)
  }

  def from(newRoot: Node): Tree = {
    // TODO potentially need to clear memory of all nodes not descending from this newRoot
    // TODO to claim that memory is required to DFS and clear all nodes skiping to DFS in newRoot.
    // TODO would be a benefit for memory (?) [profile]
    //    if (newRoot.parent.nonEmpty) newRoot.parent.get.children.clear()
    //    val rootClone = newRoot.copy(parent = None, state = newRoot.state.copy(), children = Childre/)
    //    rootClone.children.clear()
    //    for (c <- rootClone.children) c.parent = Some(rootClone)
    //    new Tree(rootClone)
    new Tree(Node(newRoot.state))
  }

  def from(newState: State): Tree = {
    new Tree(Node(newState))
  }
}
