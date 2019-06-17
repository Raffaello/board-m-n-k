package cakes.ai.mcts.tree

import cakes.game.BoardTicTacToeMcts
import org.scalatest.{Matchers, WordSpec}

class NodeSpec extends WordSpec with Matchers {

  def emptyNode(): Node = {
    val game = new BoardTicTacToeMcts
    val player: Byte = 2 // starting with 2, next will be 1.
    Node(game, player)
  }

  def emptyNodeTests(node: Node): Unit = {
    "ascending" in {
      node.ascending() should be(node)
    }

    "randomChild" in {
      node.randomChild() should be(node)
    }

    "bestChildren" in {
      node.bestChild() should be(node)
    }

    "descending" in {
      node.descending() should be(node)
    }
  }

  "TicTacToe2 Mcts" should {
    "Empty Node" should {
      val node = emptyNode()

      "be initialized properly" in {
        node.children should have length 0
        node.state.visitCount shouldBe 0
        node.state.player shouldBe 2
        node.state.score shouldBe 0.0
        node.parent shouldBe None
      }

      "be single node" should {
        emptyNodeTests(node)
      }

      "Expanded node" should {
        val node = emptyNode()
        node.expandChildren()
        val child0 = node.children.head

        "children length" in {
          node.children should have length 9
        }

        "back propagate first children" in {
          child0.backPropagate(1, 1)

          node.state.visitCount should be(1) // ???
          child0.state.visitCount shouldBe 1
        }

        "best Child should not be child0" in {
          val bestChild = node.bestChild()
          bestChild ne child0 shouldBe true
        }

        "random child should not be parent" in {
          val randChild = node.randomChild()
          randChild ne node shouldBe true
          randChild.parent should === (Some(node))
          randChild.children should be ('empty)
        }

        "descending should result" in {
          val bestChild = node.bestChild()
          val descNode = node.descending()

          bestChild eq descNode shouldBe true
          descNode.children should be ('empty)
        }

        "best Child ascending should result" in {
          val bestChild = node.bestChild()
          val ascNode = bestChild.ascending()

          ascNode eq node shouldBe true
          ascNode.parent should === (None)
        }

        "best Child parentAscending should result" in {
          val bestChild = node.bestChild()
          val bestNewRoot = bestChild.parentAscending()
          bestNewRoot.parent shouldBe Some(node)
          bestNewRoot.parent.get eq node
        }
      }
    }

    "2 layers nodes" should {
      "be valid" in {
        val root = emptyNode()
        root.state.incVisitCount()
        root.expandChildren()
        val child1 = root.bestChild()
        child1.state.incVisitCount()
        child1.expandChildren()
        val child2 = child1.bestChild()
        child2.state.incVisitCount()
        val child = root.descending()
        child.parent.get.parent.get shouldBe root
//        child2 eq child shouldBe true
      }
    }
  }
}
