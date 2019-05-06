
import ai.mcts.MctsBoard
import game.BoardTicTacToe

object Mcts extends App {


  val game = new BoardTicTacToe with MctsBoard()
  val t1 = System.currentTimeMillis()
  ai.mcts.solveTest(game)
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
  // Expected time around 30ms ??? if not need optimization.
  // Wasting time cloning board states.
}
