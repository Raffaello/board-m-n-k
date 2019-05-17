package ai.mcts

import ai.AiBoard
import cats.implicits._
import game.Position

trait MctsBoard extends AiBoard with Cloneable {
  // TODO: improve it non generating invalid moves (after game won?), or is redundant.
  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def randomMove(): Option[Position] = {
    val moves = generateMoves()
    if (moves.nonEmpty) moves.lift(scala.util.Random.nextInt(moves.size))
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
