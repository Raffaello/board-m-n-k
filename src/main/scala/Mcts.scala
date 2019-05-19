
import ai.mcts.tree.Tree
import ai.mcts.{MctsBoard, playNextMove}
import game.BoardTicTacToe2

import scala.annotation.tailrec

object Mcts extends App {

  @tailrec
  def selfPlaying(t: Option[Tree]): Option[Tree] = {
    t match {
      case None => None
      case Some(x) =>
        x.root.state.board.stdoutPrintln()
        playNextMove(x) match {
          case None => Some(x)
          case Some(y) => selfPlaying(Some(y))
        }

    }
  }

  val game = new BoardTicTacToe2 with MctsBoard
  val t = Tree(game, 2)
  val t1 = System.currentTimeMillis()
  selfPlaying(Some(t))
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
}
