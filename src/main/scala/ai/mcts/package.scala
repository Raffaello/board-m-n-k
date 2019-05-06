package ai

import ai.mcts.tree.{Node, Tree}
import game.Position

package object mcts {

  trait MctsBoard extends AiBoard with Cloneable {
    def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

    def randomMove(player: Byte): Boolean = {
      val moves = generateMoves()
      if (moves.nonEmpty) {
        moves.lift(scala.util.Random.nextInt(moves.size)) match {
          case Some(pos) => playMove(pos, player)
          case None => false
        }
      } else false
    }

    override def clone(): MctsBoard = {
      val clone = super.clone().asInstanceOf[MctsBoard]
      clone._board = this._board.map(_.clone())
      assert(clone ne this)
      assert(clone._board ne _board)

      // TODO create a "clone/copy"  method instead.
      clone.LookUps.cols = LookUps.cols.map(_.clone())
      clone.LookUps.lastPlayerIdx = LookUps.lastPlayerIdx
      clone.LookUps.rows = LookUps.rows.map(_.clone())
      clone.LookUps.won = LookUps.won
      assert(LookUps ne clone.LookUps)
      assert(LookUps.rows ne clone.LookUps.rows)
      clone
    }
  }

  final private val uctParameter: Double = Math.sqrt(2.0)

  /**
    * if visited is non zero => parentVisited > 0.
    * @param score score
    * @param visited visited
    * @param parentVisited parent visited
    * @return
    */
  def UCT(score: Double, visited: Int, parentVisited: Int): Double = {
    visited match {
      case 0 => Double.MaxValue
      case _ => score / visited.toDouble + uctParameter * Math.sqrt(Math.log(parentVisited) / visited.toDouble)
    }
  }

  def selection(node: Node): Node = node.descending()

  def expansion(node: Node): Node = {
//    assert(node.children.isEmpty)
    node.expandChildren()
    node.randomChild()
  }

  /**
    * @TODO REDESIGN THIS METHOD !!!!!!
    */
  def simulation(node: Node, player: Byte): Double = {
    if (node.state.player == player) {
      val tempNode = node.copy()
      val tempState = tempNode.state.copy()
      // TODO HERE IS MISSING TO CLONE THE BOARD....
      val tempBoard = tempState.board
      //      tempState.board = tempState.board.cloneBoard()

      var opponent = tempBoard.opponent(player)
      var iter = 0
      val maxIter = 100
      while (!tempBoard.gameEnded() && tempBoard.randomMove(opponent) && iter < maxIter) {
        opponent = tempBoard.opponent(opponent)
        iter += 1
      }

      (tempBoard.score() + 1.0) / 2.0
    } else (node.state.board.score() + 1.0) / 2.0
  }

  def backPropagation(node: Node, player: Byte, gameScore: Double): Node = {
    node.backPropagate(player, gameScore)
  }

  def findNextMoveTest(game: AiBoard, root: Node): Unit = {
    val player = root.state.player
    var process = true
    var bestNode: Node = root
    //    // TODO refactor Stats
    var totalCalls = 0
    while (process) {
      val selNode = selection(root)
      assert(selNode.children.isEmpty)
      // TODO use max time/iterations too
      // TODO review gameEnded method
      if (!selNode.state.board.gameEnded()) {
        val exploringNode = expansion(selNode)
        val gameScore = simulation(exploringNode, player)

        println(
          s"Simulation = score: $gameScore "
            + s"--- gameEnded: ${exploringNode.state.board.gameEnded()} "
            + s"-- depth: ${exploringNode.state.board.depth()}"
        )

        val tempRoot = backPropagation(exploringNode, player, gameScore)
        assert(tempRoot == root)
        totalCalls += 1
      } else {
        bestNode = selNode
        process = false
      }
    }

    println("Simulated game: ")
    bestNode.state.board.display()

    val bestRoot = bestNode.ascending()
    println("next move:")
    bestRoot.state.board.display()
    assert(bestRoot == root)

    println(s"Total Calls: $totalCalls")
  }

  def solveTest(game: MctsBoard) = {
    val player: Byte = 1
    val tree = Tree(game, player)
    val root = tree.root
    findNextMoveTest(game, root)
  }
}
