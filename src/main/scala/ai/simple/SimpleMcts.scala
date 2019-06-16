package ai.simple

import ai.simple.mcts._
import game.types.Position
import game.Implicit._

object test extends App {
  val gameState = new TicTacToeState()
  gameState.playMove(Position(1,1))
  gameState.playMove(Position(0,0))
  gameState.playMove(Position(2,0))
  gameState.playMove(Position(0,2))
  gameState.playMove(Position(0,1))
  gameState.playMove(Position(2,1))
  gameState.playMove(Position(1,2))
  println(gameState)

  /*
  next player is player 2
  state to expand is on player 2
  simulation running trought that node... player 1
  backpropagate from player 2 pov
   */

  while(gameState.allRemainingMoves().nonEmpty) {
    var m: Option[Position] = None
    //    if (gameState.lastPlayer == 1) {
    m = mctsMove(gameState, 1000, true)
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
