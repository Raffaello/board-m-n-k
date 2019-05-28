package game.boards

trait BoardPlayers {
  val numPlayers: Byte

  require(numPlayers >= 2)

  protected var _lastPlayer: Byte = numPlayers

  def lastPlayer: Byte = this._lastPlayer
}
