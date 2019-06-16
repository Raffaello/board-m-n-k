package cakes.game.types

import cakes.game.Implicit._
import cakes.game.Player
import cakes.game.boards.BoardPlayers

private[game] trait BoardMNKPType extends BoardMNType with BoardPlayers {

  final val emptyCell: Player = 0
  val k: Short
  protected val k1: Short = k - 1
  final val minWinDepth: Int = numPlayers * k1 + 1 //(numPlayers * k) - (numPlayers-1) // np*(k - 1)+1
}
