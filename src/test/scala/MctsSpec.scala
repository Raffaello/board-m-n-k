import ai.cakes.mcts.tree.Tree
import game.BoardTicTacToeMcts
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class MctsSpec extends FeatureSpec with GivenWhenThen with Matchers {

  info("As a User")
  info("I want to run a MCTS self-playing game")
  info("So I can enjoy AI")

  feature("MCTS self-playing game") {
    scenario("User execute Mcts App") {

      Given("Mcts App with def selfPlaying")
      //Mcts.game
      val game = new BoardTicTacToeMcts
      //Mcts.t
      val tree = Tree(game, 2)
      And("seed = 0")
      val seed = 0
      And("maxIter = 100")
      val maxIter = 100

      // TODO this 2 lines are not working... debug them
      tree.root.state.board.setSeed(seed)
      tree.root.state.board.maxIter = maxIter

      When("execute main (selfPlaying method)")
      val stdout = new java.io.ByteArrayOutputStream()
      var expTree: Option[Tree] = None
      Console.withOut(stdout) {
        expTree = Mcts.selfPlaying(Some(tree))
      }

      Then("The output contains")
      val expBoard = expTree.get.root.state.board
      stdout.toString.contains(expBoard.display())
      And("Final state is")
      expBoard.gameEnded() shouldBe true
      expBoard.lastPlayer shouldBe 1
      expBoard.display() shouldBe
        """ X | X | O
          | O | O | X
          | X | O | X
          |
          |""".stripMargin

    }
  }
}
