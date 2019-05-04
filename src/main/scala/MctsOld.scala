import ai.mcts.old.{BoardTicTacToeMcts, MCTS}

object MctsOld extends App {

  val game = new BoardTicTacToeMcts()
  val t1 = System.currentTimeMillis()
  MCTS.solve(game)
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
  // Expected time around 30ms ??? if not need optimization.
  // Wasting time cloning board states.
}
