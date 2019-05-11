package ai.mcts.tree

import ai.mcts.{MctsBoard, State}

final class Tree(val root: Node)

object Tree {
  def apply(game: MctsBoard, player: Byte): Tree = {
    val state = State(game.clone(), player)
    val root = Node(state, None, new Children())
    new Tree(root)
  }

  def apply(root: Node): Tree = {
    val newRoot = if (root.parent.nonEmpty) root.copy(parent = None) else root
    new Tree(newRoot)
  }

  // this could potentially override copy method too
  def update(newRoot: Node): Tree = {
    // TODO potentially need to clear memory of all nodes not descending from this newRoot
    // TODO to claim that memory is required to DFS and clear all nodes skiping to DFS in newRoot.
    // TODO would be a benefit for memory (?) [profile]
//    if (newRoot.parent.nonEmpty) newRoot.parent.get.children.clear()
    val rootClone = newRoot.copy(parent = None)
    new Tree(rootClone)
  }
}
