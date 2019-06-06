package game.boards

import game.Player

trait BoardPlayers {
  val numPlayers: Player

  protected var _lastPlayer: Player = numPlayers

  def lastPlayer: Player = _lastPlayer
}
