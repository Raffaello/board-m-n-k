
import ai.mcts.tree.Tree
import ai.mcts.{MctsBoard, playNextMove}
import game.BoardTicTacToe2

import scala.annotation.tailrec

object Mcts extends App {

  val game = new BoardTicTacToe2 with MctsBoard
  val t1 = System.currentTimeMillis()

  val t = Tree(game, 2)

  @tailrec
  def loop(t: Option[Tree]): Unit = {
    t match {
      case None =>
      case Some(x) =>
        x.root.state.board.stdoutPrintln()
        loop(playNextMove(x))
    }
  }

  loop(Some(t))
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
}
