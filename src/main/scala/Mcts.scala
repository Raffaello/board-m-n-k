import ai.mcts.MCTS
import game.BoardTicTacToe

object Mcts extends App {

  sealed class AiTicTacToe extends BoardTicTacToe with ai.AiBoard

  val game = new AiTicTacToe()
  val t1 = System.currentTimeMillis()
  MCTS.solve(game)
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
  // Expected time around 30ms ??? if not need optimization.
  // Wasting time cloning board states.
}
