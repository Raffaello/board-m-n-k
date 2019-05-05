
import game.BoardTicTacToe
import ai.AiBoard

object Mcts extends App {
   class AiTicTacToe extends BoardTicTacToe with AiBoard

  val game = new AiTicTacToe()
  val t1 = System.currentTimeMillis()
  ai.mcts.solveTest(game)
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
  // Expected time around 30ms ??? if not need optimization.
  // Wasting time cloning board states.
}
