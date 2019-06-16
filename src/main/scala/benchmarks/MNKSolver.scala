package benchmarks

import cats.implicits._
import game.BoardMNK
import game.types.BOARD_2D_ARRAY

object MNKSolver extends App {

  println("Insert the board size:")
  print("m = ")
  val m: Short = scala.io.StdIn.readShort()
  print("n = ")
  val n: Short = scala.io.StdIn.readShort()
  print("k = ")
  val k: Short = scala.io.StdIn.readShort()

  val board = BoardMNK(m, n, k, BOARD_2D_ARRAY)
  val time = System.currentTimeMillis()
  val score = ai.cakes.alphaBeta(board)
  println(s"total time: ${System.currentTimeMillis() - time}")
  println(s"Total calls: ${ai.cakes.Stats.totalCalls}")
  println({
    s"score value = $score => "
  } + {
    score match {
      case 0.0 => "STALE GAME"
      case x if x > 0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  })
  assert(score === 0.0)
}
