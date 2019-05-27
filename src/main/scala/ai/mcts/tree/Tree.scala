package ai.mcts.tree

import ai.mcts.{MctsBoard, State}

final class Tree(val root: Node)

/**
  * TODO testing for copying the tree. Is copying only the root and state
  * TODO method apply L18 and from L23 are the same, no benefit having both with just a root node cloned.
  * TODO IT should probably deeply copy each node when copied `from`
  */
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
    new Tree(Node(newRoot.state))
  }

  def from(newState: State): Tree = {
    new Tree(Node(newState))
  }
}
