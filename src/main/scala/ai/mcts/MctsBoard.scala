package ai.mcts

import ai.AiBoard
import game.Position

/**
  * todo convert to abstract class? need parameters....
  */
trait MctsBoard extends AiBoard with Cloneable {
  // TODO remove, just for debug
  def boardStatus(): String = _board.flatten.mkString

  def allPossibleMoves(): IndexedSeq[Position] = generateMoves()

  def randomMove(): Option[Position] = {
    val moves = generateMoves()
    if (moves.nonEmpty) moves.lift(scala.util.Random.nextInt(moves.size))
    else None
  }

  def playRandomMove(player: Byte): Boolean = {
    assert(player != _lastPlayer)
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
    assert(_LookUps ne clone._LookUps)
    assert(_LookUps.rows ne clone._LookUps.rows)

    clone
  }
}
