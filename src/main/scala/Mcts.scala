
import ai.mcts.{MctsBoard, playNextMove}
import ai.mcts.tree.Tree
import game.BoardTicTacToe

object Mcts extends App {

  val game = new BoardTicTacToe with MctsBoard
  val t1 = System.currentTimeMillis()

  val t = Tree(game, 2)
//  t = playNextMove(t)


  val t2 = System.currentTimeMillis()
  println(s" Total Time: ${t2 - t1} ms")
}
