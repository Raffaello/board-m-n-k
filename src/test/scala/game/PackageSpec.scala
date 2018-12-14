package game

import org.scalatest.{FlatSpec, Matchers}

class PackageSpec extends FlatSpec with Matchers {

  "Transposition table" should "be used" in {
      val game = new BoardTicTacToe()
      val trans = new TranspositionTable() {}

      val board121 = game.board.clone()
      board121(0)=  Array[Byte](1,2,1)
      board121(1) = game.board(1).clone()
      board121(2) = game.board(2).clone()
      trans.add(board121, Transposition(0, 0, Double.MaxValue, 1))
      ai.alphaBetaWithMem(trans, game)

  }
}
