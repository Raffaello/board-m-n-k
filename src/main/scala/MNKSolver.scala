import game.BoardMNK

object MNKSolver extends App {

  println("Insert the board size:")
  print("m = ")
  val m = scala.io.StdIn.readShort()
  print("n = ")
  val n = scala.io.StdIn.readShort()
  print("k = ")
  val k = scala.io.StdIn.readShort()

  val board = new BoardMNK(m, n, k)

  val score = ai.alphaBeta(board)
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
