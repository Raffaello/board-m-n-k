package ai

import game.{ABStatus, BoardMNK}

trait AlphaBeta extends AiBoard {

  private def player(maximizing: Boolean): Byte = if (maximizing) 1 else 2

//  protected def mainBlock(player: Byte, depth: Int, alpha: Int, beta: Int)(eval: ABStatus => Int): Int = {
//    if (gameEnded(depth)) {
//      val s: Int = score() * 100
//      Math.round(s + (Math.signum(s) * (1.0 / (depth + 1.0)))).toInt
//    } else {
//      val p1 = player == 1
//      var value = if(p1) Int.MinValue else Int.MaxValue
//      var a = alpha
//      var b = beta
//      consumeMoves() { p =>
//        playMove(p, player)
//        value = eval((value, p))
//        undoMove(p)
//      }
//
//      value
//    }
//  }

//  def solve(maximizing: Boolean = true, depth: Int = 0, alpha: Int = Int.MinValue, beta: Int = Int.MaxValue): Int = {
//    val cmp: (Int, Int) => Int = if (maximizing) Math.max _ else Math.min _
//    val p = player(maximizing)
////    mainBlock(p, depth, alpha, beta) { status =>
//      val value = solve(!maximizing, depth + 1, alpha, beta)
////      cmp(value, status._1)
////      a = Math.max(a, best)
//
////    }
//  }

  def nextMove(): Double = ???
}
