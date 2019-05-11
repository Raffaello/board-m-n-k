package ai.mcts

import ai.AiBoard
import ai.mcts.tree.{Node, Tree}
import game.Position

/**
  * todo convert to abstract class? need parameters....
  */
trait MctsBoard extends AiBoard with Cloneable {

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
    clone.LookUps.cols = LookUps.cols.map(_.clone())
    clone.LookUps.lastPlayerIdx = LookUps.lastPlayerIdx
    clone.LookUps.rows = LookUps.rows.map(_.clone())
    clone.LookUps.won = LookUps.won
    // TODO remove these asserts (better design required)
    assert(LookUps ne clone.LookUps)
    assert(LookUps.rows ne clone.LookUps.rows)
    clone
  }

  //  protected var _root: Tree()
  //  def root(): Tree = _root

//  def selection(): Node = root().root.descending()
/*
  def selection(node: Node): Node = node.descending()

//  def expansion(): Node = {
//    val selNode = root().root.descending()
//    selNode.expandChildren()
//    selNode.randomChild()
//  }

  def expansion(node: Node): Node = {
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
      // TODO LookUps nested object issue. (???)
      val tempBoard = tempState.board.clone()
      assert(tempBoard.LookUps() ne tempState.board.LookUps())
      assert(tempBoard.LookUps().cols ne tempState.board.LookUps().cols)

      var opponent = tempBoard.opponent(player)
      var iter = 0
      val maxIter = 10000
      while (!tempBoard.gameEnded() && tempBoard.playRandomMove(opponent) /*&& iter < maxIter*/) {
        opponent = tempBoard.opponent(opponent)
        iter += 1
      }

      (tempBoard.score() + 1.0) / 2.0
    } else (node.state.board.score() + 1.0) / 2.0
  }

  def backPropagation(node: Node, player: Byte, gameScore: Double): Node = node.backPropagate(player, gameScore)
*/
}