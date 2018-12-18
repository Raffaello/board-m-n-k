package benchmarks

import ai.TranspositionTableOld
import game.BoardMNK

object MNKSolverWithMem extends App {

  println("Insert the board size:")
  print("m = ")
  val m:Short = 3//scala.io.StdIn.readShort()
  print("n = ")
  val n:Short = 3//scala.io.StdIn.readShort()
  print("k = ")
  val k:Short = 3//scala.io.StdIn.readShort()

  val board = new BoardMNK(m, n, k) with TranspositionTableOld
//  val states = new TranspositionTable {}
  val time = System.currentTimeMillis()
  val score = ai.alphaBetaWithMem(board, board)
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

//  println(board.transpositions.mkString("\n"))
}
