import ai.mcts.{MCTS, Node, State}
import game.BoardTicTacToe

object Mcts extends App {

  val game = new BoardTicTacToe()
  val root = new Node()
  val rootState = new State()
  rootState.board = game
  rootState.player = 0 // it is not player 1, so should be player 2 (because 1st move is for p1)

  root.state = rootState
  root.parent = null

  MCTS.simulation(root, 1)
  
}
