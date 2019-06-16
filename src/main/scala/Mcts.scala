
import cakes.ai.mcts.playNextMove
import cakes.ai.mcts.tree.Tree
import game.BoardTicTacToeMcts

import scala.annotation.tailrec

object Mcts extends App {

  // TODO check the root is the current state of the game.
  @tailrec
  def selfPlaying(t: Option[Tree]): Option[Tree] = {
    t match {
      case None => None
      case Some(x) =>
        x.root.state.board.display()
        playNextMove(x) match {
          case None => Some(x)
          case Some(y) => selfPlaying(Some(y))
        }

    }
  }

  val game = new BoardTicTacToeMcts
  val t = Tree(game, 2)
  val t1 = System.currentTimeMillis()
  selfPlaying(Some(t))
  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
}
