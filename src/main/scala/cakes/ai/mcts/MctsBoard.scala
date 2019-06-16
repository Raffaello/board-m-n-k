package cakes.ai.mcts

import cakes.ai.mcts.tree.Tree
import cakes.ai.{AiBoard, mcts}
import cakes.game._
import cakes.game.boards.BoardDisplay
import cakes.game.boards.implementations.Board2dArray
import cakes.game.types.{Position, Status}
import cats.implicits._

import scala.util.Random

// TODO remove board2darray (used in clone method)
// TODO remove boarddisplay (used in self playing cakes.game)
// todo remove clonable
// todo should extends BoardMNKP
trait MctsBoard extends BoardMNKLookUp
  with BoardDisplay with Board2dArray with AiBoard with Cloneable {

  final private[mcts] val random = mcts.seed match {
    case Some(x) => new Random(x)
    case None => new Random()
  }

  private[this] var _maxIter: Int = mcts.maxIter

  def maxIter: Int = _maxIter

  def maxIter_=(v: Int): Unit = {
    require(v > 0)

    _maxIter = v
  }

  def solve: Score = {
    val tree = Tree(this, 2)
    iterate(tree)
    // todo this aiPlayer should be created on a Agent based not on a board.
    tree.root.mostVisitedDescending().state.board.score()
  }

//  override def playMove(position: Position, player: Player): Boolean = {
//    super.playMove(position, player)
//  }

  override def nextMove: Status[Score] = {
    val tree = Tree(this, this.lastPlayer)
    iterate(tree)

    // TODO FIX
    val bestnodeUCt = tree.root.bestChild()
    val bestNode = tree.root.mostVisited()
    val score = bestNode.state.board.score()
    val score2  = bestNode.mostVisitedDescending().state.board.score()
    val score3 = bestnodeUCt.mostVisitedDescending().state.board.score()
    val pos2 = bestnodeUCt.state.board.lastMove
    val pos = bestNode.state.board.lastMove

    Status[Score](score, pos)
    Status[Score](score3, pos2)
  }

  /*{
     val tree = Tree(this, 2)
     iterate(tree)
     val b = tree.root.mostVisited().state.board
     (b.score(), b.lastMove) // ???
   }*/

  // TODO: improve it non generating invalid moves (after cakes.game won?), or is redundant.
  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def setSeed(seed: Long): Unit = random.setSeed(seed)

  def randomMove(): Option[Position] = {
    val moves = generateMoves()
    if (moves.nonEmpty) moves.lift(random.nextInt(moves.size))
    else None
  }

  def playRandomMove(player: Byte): Boolean = {
    assert(player =!= _lastPlayer)
    randomMove() match {
      case Some(pos) => playMove(pos, player)
      case None => false
    }
  }

  override def clone(): MctsBoard = {
    val clone = super.clone().asInstanceOf[MctsBoard]
    clone._board = board.map(_.clone()) // <= check benchmark result
    clone._lookUps = _lookUps.deepCopy().asInstanceOf[clone.CLookUps]

    assert(clone ne this)
    assert(clone.board ne board)
    assert(_lookUps ne clone._lookUps)
    assert(_lookUps.rows ne clone._lookUps.rows)

    clone
  }
}
