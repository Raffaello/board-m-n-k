package ai.mcts

import ai.mcts.tree.Tree
import cats.implicits._
import game.{BoardTicTacToe2, Position}
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "UCT" should "evaluate to Double.MaxValue" in {
    forAll("score", "parentVisited") {
      (score: Double, parentVisited: Int) =>
        whenever(parentVisited > 0) {
          uct(score, 0, parentVisited) shouldBe Double.MaxValue
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
            uct(0.0, visited, parentVisited)
          }
        }
    }
  }

  it should "be non negative" in {
    val validDouble = for (d <- Gen.choose(0.0, Double.MaxValue)) yield d
    val validInt = for (i <- Gen.choose(1, Int.MaxValue)) yield i

    forAll(validDouble, validInt, validInt) {
      (score, visited, parentVisited) =>
        uct(score, visited, parentVisited) should be >= 0.0
    }
  }

  "MCTS TicTacToe2" should "have valid children" in {
    val game = new BoardTicTacToe2 with MctsBoard
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

  it should "have seed 0" in {
    ai.mcts.seed shouldBe Some(0)
  }

  it should "return the sub-tree" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 2)
    val bestChild = findNextMove(tree.root)

    bestChild.parent shouldBe Some(tree.root)
    tree.root.mostVisited() shouldBe bestChild

    val subTree = Tree.from(bestChild)
    subTree.root.parent shouldBe None
    subTree.root.bestChild().parent shouldBe None
  }

  it should "FindNextMove" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 2)
    val newRoot = findNextMove(tree.root)

    newRoot.state.board.depth shouldBe 1
    newRoot.children.length should be > 0
    newRoot.state.player shouldBe 1
    newRoot.parent shouldBe Some(tree.root)
    game.depth shouldBe 0
    newRoot.bestChild().parent shouldBe Some(tree.root.mostVisited())

    val subTree = Tree.from(newRoot)
    //    subTree.root.bestChild().parent shouldBe Some(subTree.root)
    subTree.root.parent shouldBe None
  }

  it should "playNextMove" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree).get

    newTree.root.state.board.depth shouldBe 1
    //    newTree.root.children.length should be > 0
    newTree.root.children.length shouldBe 0
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    game.depth shouldBe 0 // cloned
    //    newTree.root.bestChild().parent shouldBe Some(newTree.root)
  }

  it should "playNextMove player 2" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 1)
    val newTree = playNextMove(tree).get

    newTree.root.state.board.depth shouldBe 1
    //    newTree.root.children.length should be > 0
    newTree.root.children.length shouldBe 0
    newTree.root.state.player shouldBe 2
    newTree.root.parent shouldBe None
    game.depth shouldBe 0 // cloned
    //    newTree.root.bestChild().parent shouldBe Some(newTree.root)
  }

  it should "playNextMove from a began game" in {
    val game = new BoardTicTacToe2 with MctsBoard
    game.playMove((0, 0), 1)

    val tree = Tree(game, 1)
    val newTree = playNextMove(tree).get

    newTree.root.state.board.depth shouldBe 2
    //    newTree.root.children.length should be > 0
    newTree.root.children.length shouldBe 0
    newTree.root.state.player shouldBe 2
    newTree.root.parent shouldBe None
    game.depth shouldBe 1
    //    newTree.root.bestChild().parent shouldBe Some(newTree.root)
  }

  it should "playNextMove from a began game p2" in {
    val game = new BoardTicTacToe2 with MctsBoard
    game.playMove((0, 0), 1)
    game.playMove((1, 1), 2)
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree).get

    newTree.root.state.board.depth shouldBe 3
    //    newTree.root.children.length should be > 0
    newTree.root.children.length shouldBe 0
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    game.depth shouldBe 2
    //    newTree.root.bestChild().parent shouldBe Some(newTree.root)
  }

  it should "playNextMove twice" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 2)
    val newTree = playNextMove(tree).get

    newTree.root.state.board.depth shouldBe 1
    newTree.root.state.player shouldBe 1
    newTree.root.parent shouldBe None
    //    newTree.root.state.score() shouldBe 0.0

    val newTree2 = playNextMove(newTree).get
    newTree2.root.state.board.depth shouldBe 2
    newTree2.root.children.length shouldBe 0
    newTree2.root.state.player shouldBe 2
    newTree2.root.parent shouldBe None
    //    newTree2.root.state.score() shouldBe 0.0
    newTree.root.state.board.lastMove should not be newTree2.root.state.board.lastMove
  }

  it should "Play a game" in {
    val game = new BoardTicTacToe2 with MctsBoard
    val tree = Tree(game, 2)
    var t: Option[Tree] = Some(tree)
    var iter = 12
    do {
      t = playNextMove(t.get)
      iter -= 1
    } while (t.nonEmpty && iter > 0)

    iter should be > 0
    t shouldBe None
  }

  it should "move to (0,0) in this case" in {
    val game = new BoardTicTacToe2 with MctsBoard
    game.playMove((2, 2), 1)
    game.playMove((0, 1), 2)
    game.playMove((1, 1), 1)
    val tree = Tree(game, 1)
    val subTree = playNextMove(tree).get
    val p: Position = (0, 0)
    subTree.root.state.board.lastMove shouldBe p
    subTree.root.state.board.lastPlayer shouldBe 2
    subTree.root.state.board.gameEnded() shouldBe false
    subTree.root.state.player shouldBe 2
  }

  it must "draw in this game (1,2),(1,0)" in {
    val game = new BoardTicTacToe2 with MctsBoard
    game.playMove((0, 0), 1)
    game.playMove((1, 1), 2)
    game.playMove((2, 2), 1)
    game.playMove((0, 1), 2)
    game.playMove((2, 1), 1)
    game.playMove((2, 0), 2)
    game.playMove((0, 2), 1)

    val st = Tree(game, 1)
    iterate(st) //shouldBe 100
    st.root.state.board.score() shouldBe 0

  }

  it should "draw p1" in {
    val game = new BoardTicTacToe2 with MctsBoard
    game.playMove((0, 0), 1)
    game.playMove((1, 1), 2)
    game.playMove((2, 2), 1)
    game.playMove((0, 1), 2)
    game.playMove((2, 1), 1)

    val st = Tree(game, 1)
    iterate(st)
    st.root.state.board.score() shouldBe 0

  }

  it should "Draw (always?)" in {
    val game = new BoardTicTacToe2 with MctsBoard
    var player: Byte = 2
    var tree = Tree(game, player)
    var iter = 0

    while (!tree.root.state.board.gameEnded() && iter < 9) {
      tree = playNextMove(tree).get
      game.playMove(tree.root.state.board.lastMove, player)
      iter += 1
      player = game.nextPlayer()
      tree.root.state.player shouldBe player

      tree.root.state.board.depth shouldBe iter

      tree
    }

    tree.root.state.board.gameEnded() shouldBe true
    tree.root.state.board.score() shouldBe 0
    iter shouldBe 9
  }
}
