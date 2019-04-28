import ai.mcts.MCTS
import game.BoardTicTacToe

object Mcts extends App {
  sealed class AiTicTacToe extends BoardTicTacToe with ai.AiBoard

  val game = new AiTicTacToe()
  MCTS.solve(game)

}
