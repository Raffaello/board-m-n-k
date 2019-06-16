package ai.simple

import ai.simple.mcts._
import game.types.Position
import game.Implicit._

object SimpleMcts  extends App {
  val gameState = new TicTacToeState()

  while(gameState.allRemainingMoves().nonEmpty) {
    var m: Option[Position] = None
    if (gameState.lastPlayer == 1) {
      m = mctsMove(gameState, 1000)
    } else {
      m = mctsMove(gameState, 1000)
    }
    assert(m.isDefined)
    gameState.playMove(m.get)
    print(gameState)
  }

  gameState.result(gameState.lastPlayer) match {
    case 1.0 => println(s"player ${gameState.lastPlayer} win")
    case 0.0 => println(s"player ${3 - gameState.lastPlayer} win")
    case _ => println("Draw!!!")
  }
}
