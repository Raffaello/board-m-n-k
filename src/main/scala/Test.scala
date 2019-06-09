import ai.{AlphaBetaTransposition, MiniMax, TranspositionTable2dArrayString}
import game.BoardMNKP
import game.boards.BoardDisplay
import game.boards.implementations.Board2dArray

object Test extends App {
  val game = new BoardMNKP(3, 3, 3, 3) with AlphaBetaTransposition with TranspositionTable2dArrayString with Board2dArray with BoardDisplay

  while(!game.gameEnded()) {
    game.stdoutPrintln()
    val p = game.nextPlayer()
    println(s"Next Player: $p")
    val n = game.nextMove
    game.playMove(n.position, p)
  }

  game.stdoutPrintln()
}
