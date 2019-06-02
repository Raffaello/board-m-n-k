package ai.old

import game.{Board2d, BoardTicTacToe2}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class TranspositionTableSpec extends FlatSpec with Matchers {

  sealed trait TranspositionTableStub extends TranspositionTable {

    val markerAdd: mutable.Map[String, Int] = mutable.Map.empty
    val markerGet: mutable.Map[String, Int] = mutable.Map.empty

    override def add(board: Board2d, t: Transposition): Unit = {
      val h = hash(board)

      if(transpositions.isDefinedAt(h)) {
        val tt = transpositions(h)

        tt.depth shouldEqual t.depth
        tt.score shouldEqual t.score
        tt.alpha should be <= t.alpha
        tt.beta should be >= t.beta
        tt.isMaximizing shouldEqual t.isMaximizing
        markerAdd(h) = markerAdd.getOrElse(h, 0) + 1
      }

      super.add(board, t)
    }

    override def get(board: Board2d): Option[Transposition] = {
      val t = super.get(board)
      val h = hash(board)
      if (t.isDefined) {
        markerGet.update(h, markerGet.getOrElse(h, 0) + 1)
      }
      t
    }

    def getMarker(board: Board2d): Int = {
      markerAdd(hash(board))
    }
  }

  "Transposition table" should "be used" in {
    val game = new BoardTicTacToe2() with GetBoard
    val trans = new TranspositionTableStub {}
    val t = ai.old.alphaBetaWithMem(trans, game)

    t.score shouldEqual 0.0
    trans.markerGet("121000000") shouldEqual 1
    trans.markerAdd should have size 0
    t.alpha shouldEqual 0.0
    t.beta shouldEqual Double.MaxValue
  }
}
