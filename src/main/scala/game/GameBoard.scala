package game

trait GameBoard {

  def playMove(position: Position, player: Byte): Boolean

  def undoMove(position: Position, player: Byte): Boolean

  def score(): Int

  def gameEnded(): Boolean

  def gameEnded(depth: Int): Boolean

  def opponent(player: Byte): Byte

  def nextPlayer(): Byte

  def display(): String

  protected def generateMoves(): IndexedSeq[Position]

  protected def consumeMoves()(f: Position => Unit): Unit
}
