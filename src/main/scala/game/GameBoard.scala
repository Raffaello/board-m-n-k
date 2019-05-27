package game

trait GameBoard extends BoardT {

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position, player: Byte): Boolean

  def score(): Score

  def gameEnded(): Boolean

  def gameEnded(depth: Int): Boolean

  def opponent(player: Byte): Byte

  def nextPlayer(): Byte

  def display(): String

  protected def consumeMoves()(f: Position => Unit): Unit
}
