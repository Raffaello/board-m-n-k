package ai.mcts

import ai.mcts.tree.Tree
import game.BoardTicTacToe
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "UCT" should "evaluate to Double.MaxValue" in {
    forAll("score", "parentVisited") {
      (score: Double, parentVisited: Int) =>
        whenever(parentVisited > 0) {
          UCT(score, 0, parentVisited) shouldBe Double.MaxValue
        }
    }
  }

  it should "throw an IllegalArgumentException" in {
    val negativeInts = for (i <- Gen.choose(Int.MinValue, -1)) yield i
    val nonPositiveInts = for (i <- Gen.choose(Int.MinValue, 0)) yield i
    forAll(negativeInts, nonPositiveInts) {
      (visited: Int, parentVisited: Int) =>
        whenever(visited < 0 && parentVisited <= 0) {
          assertThrows[IllegalArgumentException] {
            UCT(0.0, visited, parentVisited)
          }
        }
    }
  }

  it should "be non negative" in {
    val validDouble = for (d <- Gen.choose(0.0, Double.MaxValue)) yield d
    val validInt = for (i <- Gen.choose(1, Int.MaxValue)) yield i

    forAll(validDouble, validInt, validInt) {
      (score, visited, parentVisited) =>
        UCT(score, visited, parentVisited) should be >= 0.0
    }
  }

  "MCTS TicTacToe" should "have valid children" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    tree.root.expandChildren()
    tree.root.state.incVisitCount()
    for (child <- tree.root.children) {
      child.state.incVisitCount()
      child.expandChildren()
    }
    for (child <- tree.root.children) {
      child.parent shouldBe Some(tree.root)
      for (child2 <- child.children) {
        child2.parent.get.parent shouldBe Some(tree.root)
      }
    }

    tree.root.bestChild().parent shouldBe Some(tree.root)
    tree.root.bestChild().bestChild().parent.get.parent shouldBe Some(tree.root)
  }

  it should "return the sub-tree" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val bestChild = findNextMove(tree.root)

    bestChild.parent shouldBe Some(tree.root)
    tree.root.bestChild() shouldBe bestChild

    val subTree = Tree.update(bestChild)
    subTree.root.parent shouldBe None
    subTree.root.bestChild().parent shouldBe None
  }

  it should "FindNextMove" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val newRoot = findNextMove(tree.root)

    newRoot.state.board.depth() shouldBe 1
    newRoot.children.length should be > 0
    newRoot.state.player shouldBe 1
    newRoot.parent shouldBe Some(tree.root)
    game.depth() shouldBe 0
    newRoot.bestChild().parent shouldBe Some(tree.root.bestChild())

    val subTree = Tree.update(newRoot)
//    subTree.root.bestChild().parent shouldBe Some(subTree.root)
    subTree.root.parent shouldBe None
  }

  it should "playNextMove" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree)

    newTree.root.state.board.depth() shouldBe 1
//    newTree.root.children.length should be > 0
    newTree.root.children.length shouldBe 0
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    game.depth() shouldBe 0 // cloned
//    newTree.root.bestChild().parent shouldBe Some(newTree.root)
  }

  it should "playNextMove twice" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree)

    newTree.root.state.board.depth() shouldBe 1
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
//    newTree.root.state.score() shouldBe 0.0

    val newTree2 = playNextMove(newTree)
    newTree2.root.state.board.depth() shouldBe 2
    newTree2.root.children.length shouldBe  0
    newTree2.root.state.player shouldBe 2
    newTree2.root.parent shouldBe None
//    newTree2.root.state.score() shouldBe 0.0
    newTree.root.state.board.lastMove() should not be newTree2.root.state.board.lastMove()
  }

  // TODO It seems an error in the player to be propagate back... like call once more opponent...
  // TODO from the tree seems correct, but displayed with p1 'X', instead of 'O' p2 ??? debug
  it should "draw p1" in {
    val game = new BoardTicTacToe with MctsBoard
    game.playMove((0,0), 1)
    game.playMove((0,1), 2)
    game.playMove((1,1), 1)

    val tree = Tree(game,1)
    val subTree = playNextMove(tree)

    subTree.root.state.board.lastMove() shouldBe (2,2)
    subTree.root.state.board.gameEnded() shouldBe false
  }

  // TODO fix it, the MCTS UCT seems unbalanced
  it should "Draw" in {
    val game = new BoardTicTacToe with MctsBoard
    var player: Byte = 2
    var tree = Tree(game, player)
    var iter = 0

    while (!tree.root.state.board.gameEnded() && iter < 9) {
      tree = playNextMove(tree)
      game.playMove(tree.root.state.board.lastMove(), player)
      iter += 1
      player = tree.root.state.board.opponent(player)
      tree.root.state.player shouldBe player
      tree.root.state.board.depth() shouldBe iter

      tree
    }

    tree.root.state.board.gameEnded() shouldBe true
    tree.root.state.board.score() shouldBe 0
    iter shouldBe 9
  }
}
