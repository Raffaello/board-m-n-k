import game.{BoardMNK, TranspositionTable}

object MNKSolverWithMem extends App {

  println("Insert the board size:")
  print("m = ")
  val m:Short = 3//scala.io.StdIn.readShort()
  print("n = ")
  val n:Short = 3//scala.io.StdIn.readShort()
  print("k = ")
  val k:Short = 3//scala.io.StdIn.readShort()

  val board = new BoardMNK(m, n, k)
  val states = new TranspositionTable {}
  val time = System.currentTimeMillis()
  val score = ai.alphaBetaWithMem(states, board)
  println(s"total time: ${System.currentTimeMillis() - time}")
  println(s"Total calls: ${ai.Stats.totalCalls}")
  println(s"Total cache: ${states.transpositions.size}")
  println({
    s"score value = $score => "
  } + {
    score match {
      case 0.0 => "STALE GAME"
      case x if x>0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  })

//  println(states.transpositions.mkString("\n"))
}
