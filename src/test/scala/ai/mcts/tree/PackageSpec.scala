package ai.mcts.tree

import ai.mcts.MctsBoard
import game.BoardTicTacToe
import org.scalatest.{Matchers, WordSpec}

class PackageSpec extends WordSpec with Matchers {
  def emptyTree(): Tree = {
    val game = new BoardTicTacToe() with MctsBoard
    val player: Byte = 1
    Tree(game, player)
  }

  def emptyRootTests(root: Node): Unit = {
    "ascending" in {
      root.ascending() should be(root)
    }

    "randomChild" in {
      root.randomChild() should be(root)
    }

    "bestCgildren" in {
      root.bestChildren() should be(root)
    }

    "descending" in {
      root.descending() should be(root)
    }
  }

  "TicTacToe Mcts" should {
    "Empty Tree" should {
      val tree = emptyTree()

      "be initialized properly" in {
        tree.root.children should have length 0
        tree.root.state.visitCount shouldBe 0
        tree.root.state.player shouldBe 1
        tree.root.state.score shouldBe Double.MinValue
        tree.root.parent shouldBe None
      }

      "be single root node tree" should {
        emptyRootTests(tree.root)
      }
    }

    "Expanded root" should {
      val tree = emptyTree()
      tree.root.expandChildren()
      val child0 = tree.root.children.head

      "children length" in {
        tree.root.children should have length 9
      }

      "back propagate first children" in {
        child0.backPropagate(1, 0.5)

        tree.root.state.visitCount should be(1) // ???
      }

      "updating to first child as a new root" should {
        val newTree = Tree.update(child0)
        emptyRootTests(newTree.root)
        "new root not equal old root" in {
          newTree.root should not be tree.root
        }
      }
    }
  }
}
