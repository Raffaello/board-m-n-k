package benchmarks

import ai.old.BoardMNKwithGetBoard

object MNKSolverWithMem extends App {

  println("Insert the board size:")
  print("m = ")
  val m:Short = 3//scala.io.StdIn.readShort()
  print("n = ")
  val n:Short = 3//scala.io.StdIn.readShort()
  print("k = ")
  val k:Short = 3//scala.io.StdIn.readShort()

  val board = new BoardMNKwithGetBoard(m, n, k) with ai.old.TranspositionTable
//  val states = new TranspositionTable {}
  val time = System.currentTimeMillis()
  val score = ai.old.alphaBetaWithMem(board, board)
  println(s"total time: ${System.currentTimeMillis() - time}")
  println(s"Total calls: ${ai.Stats.totalCalls}")
  println(s"Total cache: ${board.transpositions.size}")
  println({
    s"score value = $score => "
  } + {
    score.score match {
      case 0.0 => "STALE GAME"
      case x if x>0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  })
  assert(score.score == 0.0)
//  println(board.transpositions.mkString("\n"))
}
