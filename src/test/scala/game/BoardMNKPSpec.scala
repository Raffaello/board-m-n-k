package game

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
        val game = new BoardMNKP(m, n, k, p)
        game.lastPlayer shouldBe p

        for (i <- NumericRange.inclusive[Short](1, p, 1)) {
          val np = game.nextPlayer()
          np shouldBe i
          game.playMove((i, 0), np) shouldBe true
        }
      }
    }
  }
}
