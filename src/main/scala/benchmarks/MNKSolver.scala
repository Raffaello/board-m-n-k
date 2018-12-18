package benchmarks

import game.BoardMNK

object MNKSolver extends App {

  println("Insert the board size:")
  print("m = ")
  val m:Short = 3 //scala.io.StdIn.readShort()
  print("n = ")
  val n:Short = 3 //scala.io.StdIn.readShort()
  print("k = ")
  val k:Short = 3 //scala.io.StdIn.readShort()

  val board = new BoardMNK(m, n, k)
  val time = System.currentTimeMillis()
  val score = ai.alphaBeta(board)
  println(s"total time: ${System.currentTimeMillis() - time}")
  println(s"Total calls: ${ai.Stats.totalCalls}")
  println({
    s"score value = $score => "
  } + {
    score match {
      case 0.0 => "STALE GAME"
      case x if x>0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  })
}
