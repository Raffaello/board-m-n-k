package game

import ai.cakes.MiniMax
import game.Implicit.convertToPlayer
import game.boards.implementations.Board2dArray
import game.types.{Position, Status}
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.NumericRange

class BoardMNKPSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "BoardMNKP" should "compute nextPlayer correctly" in {
    val maxP: Byte = Byte.MaxValue
    val m: Short = (maxP + 1).toShort
    val n: Short = 3
    val k: Short = 3

    val ps = for (i <- Gen.choose[Byte](2, maxP)) yield i

    forAll(ps) { p: Byte =>
      whenever(p >= 2) {
        val game = new BoardMNKP(m, n, k, p) with Board2dArray
        game.lastPlayer shouldBe p

        for (i <- NumericRange.inclusive[Short](1, p, 1)) {
          val np = game.nextPlayer()
          np shouldBe i
          game.playMove(Position(i, 0), np) shouldBe true
        }
      }
    }
  }

  "BoardMNKP(3,3,3,2)" should "compute valid score P1 win By Row" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.playMove(Position(0, 0), 1) shouldBe true
    game.playMove(Position(1, 0), 2) shouldBe true
    game.playMove(Position(0, 1), 1) shouldBe true
    game.playMove(Position(2, 0), 2) shouldBe true
    game.playMove(Position(0, 2), 1) shouldBe true

    game.score(1) shouldEqual 1
    game.score(2) shouldEqual -1
  }

  "BoardMNKP(3,3,3,2)" should "compute valid score P1 win By Col" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.playMove(Position(0, 0), 1) shouldBe true
    game.playMove(Position(0, 1), 2) shouldBe true
    game.playMove(Position(1, 0), 1) shouldBe true
    game.playMove(Position(0, 2), 2) shouldBe true
    game.playMove(Position(2, 0), 1) shouldBe true

    game.score(1) shouldEqual 1
    game.score(2) shouldEqual -1
  }

  "BoardMNKP(3,3,3,2)" should "compute valid score P1 win By DiagSE" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.playMove(Position(0, 0), 1) shouldBe true
    game.playMove(Position(1, 0), 2) shouldBe true
    game.playMove(Position(1, 1), 1) shouldBe true
    game.playMove(Position(2, 0), 2) shouldBe true
    game.playMove(Position(2, 2), 1) shouldBe true

    game.score(1) shouldEqual 1
    game.score(2) shouldEqual -1
  }

  "BoardMNKP(3,3,3,2)" should "compute valid score P1 win By DiagNE" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.playMove(Position(0, 2), 1) shouldBe true
    game.playMove(Position(1, 0), 2) shouldBe true
    game.playMove(Position(1, 1), 1) shouldBe true
    game.playMove(Position(1, 2), 2) shouldBe true
    game.playMove(Position(2, 0), 1) shouldBe true

    game.score(1) shouldEqual 1
    game.score(2) shouldEqual -1
  }

  it should "compute valid score P2 win by Col" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.playMove(Position(0, 0), 1) shouldBe true
    game.playMove(Position(0, 1), 2) shouldBe true
    game.playMove(Position(1, 0), 1) shouldBe true
    game.playMove(Position(1, 1), 2) shouldBe true
    game.playMove(Position(2, 2), 1) shouldBe true
    game.playMove(Position(2, 1), 2) shouldBe true

    game.score(2) shouldEqual 1
    game.score(1) shouldEqual -1
  }

  it should "compute valid score draw" in {
    val game = new BoardMNKP(3, 3, 3, 2) with Board2dArray
    game.score(1) shouldEqual 0
    game.score(2) shouldEqual 0
  }

  it should "draw using Minimax with 3, 3, 3, 2" in {
    val game = new BoardMNKP(3, 3, 3, 2) with MiniMax with Board2dArray
    game.solve shouldBe 0
  }

  it should "compute next first move" in {
    val game = new BoardMNKP(3, 3, 3, 2) with MiniMax with Board2dArray
    game.nextPlayer() shouldBe 1
    game.nextMove shouldEqual Status(0, Position(0, 0))
  }

  it should "compute next second move" in {
    val game = new BoardMNKP(3, 3, 3, 2) with MiniMax with Board2dArray
    game.playMove(Position(0, 0), game.nextPlayer()) shouldBe true
    game.nextPlayer() shouldBe 2
    game.nextMove shouldEqual Status(0, Position(1, 1))
  }

  "BoardMNKP(3,3,3,3)" should "draw" in {
    val game = new BoardMNKP(3, 3, 3, 3) with MiniMax with Board2dArray
    game.nextPlayer() shouldBe 1
    game.solve shouldBe 0
  }
}
