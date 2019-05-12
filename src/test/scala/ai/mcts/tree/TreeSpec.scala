package ai.mcts.tree

import ai.mcts.MctsBoard
import game.BoardTicTacToe2
import org.scalatest.{Matchers, WordSpec}

/**
  * Basically a clone of NodeSpec, but with a starting point of Tree structure
  * That should be the only one used outside the ai.mcts.tree package
  * NodeSpec should be not accessible outside mcts pacakge
  */
class TreeSpec extends WordSpec with Matchers {
  def emptyTree(): Tree = {
    val game = new BoardTicTacToe2() with MctsBoard
    val player: Byte = 2 // starting with 2, next will be 1.
    Tree(game, player)
  }

  def emptyRootTests(root: Node): Unit = {
    "ascending" in {
      root.ascending() should be(root)
    }

    "randomChild" in {
      root.randomChild() should be(root)
    }

    "bestChild" in {
      root.bestChild() should be(root)
    }

    "descending" in {
      root.descending() should be(root)
    }

    // TODO remove the method. profilig if it improve performances.
    "parentAscending" in {
      root.parentAscending() shouldBe root.bestChild()
    }
  }

  "TicTacToe2 Mcts" should {
    "Empty Tree" should {
      val tree = emptyTree()

      "be initialized properly" in {
        tree.root.children should have length 0
        tree.root.state.visitCount shouldBe 0
        tree.root.state.player shouldBe 2
        tree.root.state.score shouldBe 0.0
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

        tree.root.state.visitCount should be(1)
        child0.state.visitCount should be(1)
      }

      "updating to first child as a new root" should {
        val newTree: Tree = Tree.from(child0)
        emptyRootTests(newTree.root)
        "new root " should {
          "not equal old root" in {
            newTree.root should not be tree.root
          }

          "parent None" in {
            newTree.root.parent shouldBe None
          }

          "clone from child0" in {
            newTree.root ne child0 shouldBe true
          }
        }

        "expanding further" in {
          val newTree2 = Tree.from(child0)
          newTree2.root.expandChildren()
          newTree2.root.bestChild().expandChildren()
          val node = newTree2.root.descending()

          node.ascending() eq newTree2.root shouldBe true
          newTree2.root.bestChild().bestChild() eq node shouldBe true

          newTree2.root.bestChild().parent shouldBe Some(newTree2.root)
          newTree2.root.bestChild().bestChild().parent.get.parent shouldBe Some(newTree2.root)
        }
      }
    }
  }
}
