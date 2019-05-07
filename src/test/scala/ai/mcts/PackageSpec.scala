package ai.mcts

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
    forAll("visisted", "parentVisited") {
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

  "MCTS TicTacToe" should "Draw" in {
    val game = new BoardTicTacToe
    var player = 1

  }
}
