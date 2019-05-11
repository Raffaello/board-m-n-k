package ai.mcts

import ai.AiBoard
import ai.mcts.tree.{Node, Tree}
import game.{Position, Score}

/**
  * todo convert to abstract class? need parameters....
  */
trait MctsBoard extends AiBoard with Cloneable {

//  protected var _root: Tree = Tree(this, 2)

//  protected def remapScore(score: Score): Double = (score + 1.0) / 2.0
//  protected def remapScore(board: MctsBoard): Double = remapScore(board.score())

  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def randomMove(): Option[Position] = {
    val moves = generateMoves()
    if (moves.nonEmpty) moves.lift(scala.util.Random.nextInt(moves.size))
    else None
  }

  def playRandomMove(player: Byte): Boolean = {
    randomMove() match {
      case Some(pos) => playMove(pos, player)
      case None => false
    }
  }

  override def clone(): MctsBoard = {
    val clone = super.clone().asInstanceOf[MctsBoard]
    clone._board = this._board.map(_.clone())
    clone._LookUps = clone._LookUps.clone()
    assert(clone ne this)
    assert(clone._board ne _board)

    // TODO create a "clone/copy"  method instead.
    clone._LookUps.cols = _LookUps.cols.map(_.clone())
    clone._LookUps.lastPlayerIdx = _LookUps.lastPlayerIdx
    clone._LookUps.rows = _LookUps.rows.map(_.clone())
    clone._LookUps.won = _LookUps.won
    // TODO remove these asserts (better design required)
    assert(_LookUps ne clone._LookUps)
    assert(_LookUps.rows ne clone._LookUps.rows)

    clone
  }

//  def root(): Tree = _root
//
//  protected def selection(): Node = root().root.descending()
//
//  def selection(node: Node): Node = node.descending()
//
//  def expansion(node: Node): Node = {
//    node.expandChildren()
//    node.randomChild()
//  }
//
//  protected def simulation(): Double = {
//    val node = root().root
//    val player = node.state.player
//
//    if (node.state.player == player) {
//
//      val tempNode = node.copy()
//      val tempState = tempNode.state.copy()
//      val tempBoard = tempState.board.clone()
//
//      assert(tempBoard.LookUps() ne tempState.board.LookUps())
//      assert(tempBoard.LookUps().cols ne tempState.board.LookUps().cols)
//
//      var opponent = tempBoard.opponent(player)
//      var iter = 0
//      // TODO load from config
//      val maxIter = 10
//      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent) && iter < maxIter ) {
//        opponent = tempBoard.opponent(opponent)
//        iter += 1
//      }
//
//      remapScore(tempBoard)
//    } else remapScore(node.state.board.score())
//  }
//
//  def simulation(node: Node, player: Byte): Double = {
//    if (node.state.player == player) {
//
//      val tempNode = node.copy()
//      val tempState = tempNode.state.copy()
//      val tempBoard = tempState.board.clone()
//
//      assert(tempBoard.LookUps() ne tempState.board.LookUps())
//      assert(tempBoard.LookUps().cols ne tempState.board.LookUps().cols)
//
//      var opponent = tempBoard.opponent(player)
//      var iter = 0
//      // TODO load from config
//      val maxIter = 10
//      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent) /*&& iter < maxIter*/ ) {
//        opponent = tempBoard.opponent(opponent)
//        iter += 1
//      }
//
//      remapScore(tempBoard)
//    } else remapScore(node.state.board)
//  }
//
//  def backPropagation(node: Node, player: Byte, gameScore: Double): Node = node.backPropagate(player, gameScore)
//  protected def backPropagation(node: Node, gameScore: Double): Node = node.backPropagate(root().root.state.player, gameScore)
//
//  def findNextMove(): Tree = {
//    val root = _root.root
//    val player = root.state.player
//    var process = true
//    var bestNode: Node = root
//    Stats.totalCalls = 0
//
//    while (process) {
//      val selNode = selection()
//      assert(selNode.children.isEmpty)
//      if (!selNode.state.board.gameEnded()) {
//        val exploringNode = expansion(selNode)
//        val gameScore = simulation(exploringNode, player)
//
//        val tempRoot = backPropagation(exploringNode, gameScore)
//        assert(tempRoot == root)
//        Stats.totalCalls += 1
//      } else {
//        bestNode = selNode
//        process = false
//      }
//    }
//
//    val bestRootNode = bestNode.parentAscending()
//    assert(bestRootNode.parent.get == root)
//
//    val newRoot: Node = bestRootNode
//    _root = Tree.update(newRoot)
//    _root
//  }
}