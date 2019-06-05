package game.boards

trait BoardPlayers {
  val numPlayers: Byte

  protected var _lastPlayer: Byte = numPlayers

  def lastPlayer: Byte = _lastPlayer
}
