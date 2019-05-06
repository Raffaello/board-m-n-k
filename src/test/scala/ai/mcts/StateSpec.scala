package ai.mcts

import game.BoardTicTacToe
import org.scalatest.{Matchers, WordSpec}

class StateSpec extends WordSpec with Matchers {

  def initState(): State = {
    val game = new BoardTicTacToe() with MctsBoard
    val player: Byte = 2
    State(game, player)
  }

  "TicTacToe Mcts State" should {
    "be initialiazed" in {
      val state = initState()
      state.visitCount shouldBe 0
      state.score shouldBe 0.0
      state.player shouldBe 2
      state.board.depth() shouldBe 0
    }

    "generate all the states" should {
      "immutably and not mutate root state" in {
        val state = initState()
        state.allPossibleStates()
        state.board.depth() shouldBe 0
      }

      "have length 9" in {
        val states = initState().allPossibleStates()
        states should have length 9
      }

      "be set to opponent player" in {
        val state = initState()
        val states = state.allPossibleStates()

        states.foreach(s => s.player shouldBe state.opponent())
      }

      "should be deep cloned" in {
        val state = initState()
        val states = state.allPossibleStates()


        for {
          i <- states.indices
          j <- 0 to 1
          if i < j
        } {
          val s1 = states(i)
          val s2 = states(j)

          state eq s1 shouldBe false
          state eq s2 shouldBe false

          s1 ne s2 shouldBe true

          val b1 = s1.board
          val b2 = s2.board

          b1 ne b2 shouldBe true

          b1.depth() shouldBe 1
          b2.depth() shouldBe 1
        }
      }

    }
  }
}
