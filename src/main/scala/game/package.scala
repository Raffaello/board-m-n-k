package object game {
  type Board2D = Array[Array[Byte]]
  type Board1D = Array[Byte]
  type Position = (Short, Short)
  type PositionInt = (Int, Int)
  type Status = (Int, Position)


  abstract class withBoard2D(val m: Short, val n: Short) {
    protected val _board: Board2D = Array.ofDim[Byte](m, n)

    def board(position: Position) = _board(position._1)(position._2)
    /**
      * @deprecated
      */
    def getBoard2D(): Board2D = _board

  }

  abstract class withBoard1D(val m: Short, val n: Short) {
    private val _board: Board1D = Array.ofDim[Byte](m * n)

    def board(position: Position) = _board((position._1 * m) + position._2)
  }

}
