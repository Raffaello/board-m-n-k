package ai.mcts

import ai.AiBoard
import game.Position

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