package ai.mcts

import ai.AiBoard
import ai.mcts.tree.{Node, Tree}
import cats.implicits._
import game.{Position, Score}

import scala.annotation.tailrec
import scala.util.Random

trait MctsBoard extends AiBoard with Cloneable {
  final private[this] val random = new Random()
  ai.mcts.seed match {
    case Some(x) => setSeed(x)
    case None =>
  }

  def iterate(node: Node): Int = {
    @tailrec
    def loop(iter: Int = 0): Int = {
      if (iter < ai.mcts.maxIter) {
        val selNode = selection(node)
        val expNode = expansion(selNode)
        val gameScore = simulation(expNode)
        backPropagation(expNode, gameScore)
        loop(iter + 1)
      } else iter
    }

    loop()
  }

  def iterate(tree: Tree): Int = iterate(tree.root)

  def solve: Score = {
    val tree = Tree(this, 1)
    iterate(tree)
    tree.root.mostVisitedDescending().state.board.score()
  }

  // TODO: improve it non generating invalid moves (after game won?), or is redundant.
  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def setSeed(seed: Long): Unit = random.setSeed(seed)

  def randomMove(): Option[Position] = {
    val moves = generateMoves()
    if (moves.nonEmpty) moves.lift(random.nextInt(moves.size))
    else None
  }

  def playRandomMove(player: Byte): Boolean = {
    require(player =!= _lastPlayer)
    randomMove() match {
      case Some(pos) => playMove(pos, player)
      case None => false
    }
  }

  override def clone(): MctsBoard = {
    val clone = super.clone().asInstanceOf[MctsBoard]
    clone._board = _board.map(_.clone()) // <= check benchmark result
    clone._lookUps = _lookUps.clone().asInstanceOf[clone.CLookUps]

    assert(clone ne this)
    assert(clone._board ne _board)
    assert(_lookUps ne clone._lookUps)
    assert(_lookUps.rows ne clone._lookUps.rows)

    clone
  }
}
