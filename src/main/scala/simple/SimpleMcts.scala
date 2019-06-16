package simple

import simple.mcts._
import cakes.game.types.Position
import cakes.game.Implicit._

object SimpleMcts extends App {
  val gameState = new TicTacToeState()

  while(gameState.allRemainingMoves().nonEmpty) {
    var m: Option[Position] = None
//    if (gameState.lastPlayer == 1) {
      m = mctsMove(gameState, 1000, false)
//    } else {
//      m = mctsMove(gameState, 10000)
//    }
    assert(m.isDefined)
    gameState.playMove(m.get)
    println(s"Best Move: $m")
    println(gameState)
  }

  gameState.result(gameState.lastPlayer) match {
    case 1.0 => println(s"player ${gameState.lastPlayer} win")
    case 0.0 => println(s"player ${3 - gameState.lastPlayer} win")
    case _ => println("Draw!!!")
  }
}
