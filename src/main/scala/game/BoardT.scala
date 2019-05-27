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


trait BoardT {
//  def board

  protected def board(pos: Position): Byte

  protected def board(pos: Position)(p: Player): Unit

  protected def generateMoves(): IndexedSeq[Position]
}
