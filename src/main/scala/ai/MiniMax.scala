package ai

import cats.implicits._
import game.types.{Position, Status}
import game.{BoardMNKP, Score, nilPosition}

trait MiniMax extends BoardMNKP with AiBoard with AiBoardScoreEval {

  protected def mainBlock(player: Byte)(eval: Status[Score] => Score): Score = {
    if (gameEnded()) {
      scoreEval(aiPlayer)
    } else {
      Stats.totalCalls += 1
      var value = initValue(player)

      consumeMoves { p =>
        playMove(p, player)
        value = eval(Status(value, p))
        undoMove(p, player)
      }

      value
    }
  }


  /**
    * aiPlayer to max
    */
  def solve(maximizing: Boolean): Score = {
    lazy val cmp: (Int, Int) => Int = super.cmp(maximizing)
    val player = nextPlayer()

    mainBlock(player) { status =>
      val value = solve(nextPlayer() === aiPlayer)
      cmp(status.score, value)
    }
  }

  def solve: Score = solve(nextPlayer() === aiPlayer)

  override def nextMove: Status[Score] = nextMove(nextPlayer() === aiPlayer)

  protected def nextMove(maximizing: Boolean): Status[Score] = {
    var pBest: Position = nilPosition
    val player = nextPlayer()

    // todo mainBlock should return to avoid var
    val score = mainBlock(player) { status: Status[Score] =>
      val newValue = solve(nextPlayer() === aiPlayer)
      val sig = signum(maximizing)
      if (status.score * sig < newValue * sig) {
        pBest = status.position
        newValue
      } else status.score
    }

    Status(score, pBest)
  }
}
