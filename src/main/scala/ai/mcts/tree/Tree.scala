package ai.mcts.tree

import ai.mcts.{MctsBoard, State}

final case class Tree(root: Node)

object Tree {
  def apply(game: MctsBoard, player: Byte): Tree = {
    val state = State(game, player)
    val root = Node(state, None, new Children())
    new Tree(root)
  }

  def update(newRoot: Node): Tree = {
    val rootClone = newRoot.copy(parent = None)
    new Tree(rootClone)
  }
}
