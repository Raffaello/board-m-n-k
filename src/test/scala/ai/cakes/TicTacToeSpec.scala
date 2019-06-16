package ai.cakes

import game.Implicit.convertToPlayer
import game.Score
import game.types.{Position, Status}
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

abstract class TicTacToeSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  def solveTheGame(game: AiBoard): Unit = {
    "solve the game" in new AiTicTacToeExpectedStats {
      game.solve shouldEqual 0
      game match {
        case game: MiniMax => expMiniMax(game.Stats.totalCalls)
        case game: NegaMax => expNegamax(game.Stats.totalCalls)
        case game: AlphaBetaTransposition => expAlphaBetaTTTrait(game.Stats.totalCalls, game.Stats.cacheHits, game.transpositions.size)
        case game: AlphaBeta => expAlphaBeta(game.Stats.totalCalls)
        case _ => ???
      }
    }
  }

  def drawTheGame(game: AiBoard, exp: Int => Unit): Unit = {
    "draw the game" in new AiTicTacToeExpectedStats {
      game.solve shouldEqual 0
      exp(game.Stats.totalCalls)
    }
  }

  def haveFirstMove(game: AiBoard): Unit = {
    "have first move" in {
      val s: Status[Score] = Status(0, Position(0, 0))
      game.nextMove shouldEqual s
    }
  }

  def have2ndMove(game: AiBoard): Unit = {
    "have 2nd move" in {
      val s: Status[Score] = Status(0, Position(1, 1))
      game.playMove(Position(0, 0), 1)
      game.nextMove shouldEqual s
    }
  }

  def aiBoard(game: AiBoard): Unit = {
    s"${game.getClass.getSimpleName}" should {
      solveTheGame(game)
      haveFirstMove(game)
      have2ndMove(game)
    }
  }
}
