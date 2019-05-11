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

  "MCTS TicTacToe" should "FindNextMove" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val newTree = findNextMove(tree)

    newTree.root.state.board.depth() shouldBe 1
    newTree.root.children.length should be > 0
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    game.depth() shouldBe 0
  }

  it should "playNextMove" in {
    val game = new BoardTicTacToe with MctsBoard
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree)

    newTree.root.state.board.depth() shouldBe 1
    newTree.root.children.length should be > 0
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    game.depth() shouldBe 0 // cloned
  }

  it should "Draw" in {
    val game = new BoardTicTacToe with MctsBoard
    var player: Byte = 2
    var tree = Tree(game, player)
    var iter = 0

    tree = playNextMove(tree)

    tree.root.state.board.depth() shouldBe 1
    tree.root.children.length should be > 0
    tree.root.state.player shouldBe 1
    tree.root.parent shouldBe None
//    game.depth() shouldBe 1


    tree = playNextMove(tree)
    tree.root.state.board.depth() shouldBe 2
    tree.root.state.player shouldBe 2
    tree.root.parent shouldBe None
//    game.depth() shouldBe 2

//    while (!tree.root.state.board.gameEnded() && iter < 9) {
//      tree = playNextMove(game, tree)
//
//      iter += 1
//      player = (3 - player).toByte
//      tree.root.state.player shouldBe player
//      tree.root.state.board.depth() shouldBe iter
//    }

//    tree.root.state.board.gameEnded() shouldBe true
//    tree.root.state.board.score() shouldBe 0.0
//    iter should be < 9

  }
}
