package game

/*
//  trait Board[T] {
  trait Board {
//    def board: T
    def board: Board2d
    protected def board(pos: Position): Byte

    //  protected def setBoard(pos: Position, p: Player): Unit
    protected def board(pos: Position)(p: Player): Unit

    def flatten: Board1d
  }
*/
//  implicit object Board1dImpl extends Board[Board1d]
//  implicit object Board2dImpl extends Board[Board2d]





// TODO use a type class?
//trait BoardT[T <: BoardMNType] {
trait BoardT {
  protected def board(pos: Position): Byte

  protected def board_=(pos: Position)(p: Player): Unit

  def generateMoves(): IndexedSeq[Position]

  def display(): String
}
