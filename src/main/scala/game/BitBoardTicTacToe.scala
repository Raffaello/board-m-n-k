package game

class BitBoardTicTacToe extends BoardT with GameBoard {
  val m = 3
  val n = 3
  type BitBoard = Int
  var _board: BitBoard = 0

  protected var _lastPlayer: Byte = 2
  protected val minWinDepth: Int = 4 //(2 * k) - 2 // 2*(k-1) // 2*k1 // zero-based depth require to subtract 1 extra more
  protected var _depth: Int = 0
  protected var freePositions: Int = m * n
  protected var _lastMove: Position = (0, 0)

  def depth: Int = _depth

  def lastPlayer: Byte = this._lastPlayer

  override def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = position
    // 0 0 0
    val value: BitBoard = (1 << (row*m + col) ) << (player -1)

    if ((_board & value) > 0) false
    else {
      _board ^= value
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  override def undoMove(position: (Short, Short), player: Player): Boolean = {
    val (i, j) = position
    val (row, col) = (i,j)
    val value: BitBoard = (1 << (row*m + col) ) << (player -1)

    if ((_board & value) > 0) {
      _board ^= value
      freePositions += 1
      _depth -= 1
      true
    } else false
  }

  override def gameEnded(depth: Score): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions == 0) true
    else checkWin()
  }

  override def opponent(player: Player): Player = ???

  override def nextPlayer(): Player = ???

  override def display(): String = ???

  override protected def consumeMoves()(f: ((Short, Short)) => Unit): Unit = ??? //generateMoves().foreach(f)


  protected def scoreDiagsTL(): Int = {
    // 1 0 0 | 0 1 0 | 0 0 1 || 1 0 0 | 0 1 0 | 0 0 1
    // 1 + 16 + 255 || 2^9 + 2^13 + 2^17
    //  273 p1  || 139776 p2
    if ((_board & 273) > 0) 1
    else if ((_board & 139776) > 0) 2
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    // 0 0 1 | 0 1 0 | 1 0 0 || 0 0 1 | 0 1 0 | 1 0 0
    // 4 + 16 + 64 || 2^11 + 2^13 + 2^15
    //  84 p1  || 43008 p2
    if ((_board & 84) > 0) 1
    else if ((_board & 43008) > 0) 2
    else 0
  }

  def scoreRows: Int = {
    // 1 1 1 || 1 1 1
    // 1+2+4 ||
    if ((_board & 7)> 0 ||(_board & 56) > 0 || (_board & 448) > 0) 1
    else if ((_board & 3584)> 0 ||(_board & 28672) > 0 || (_board & 229376) > 0) 2
    else 0
  }

  def scoreCols: Int = {
    // 1 0 0 | 1 0 0 | 1 0 0
    // 0 1 0 | 0 1 0 | 0 1 0
    // 0 0 1 | 0 0 1 | 0 0 1
    // 0 0 0   0 0 0   0 0 0 1 0 0 1 0 0 1 0 0
    // 1 + 8 + 64 = 73
    if ((_board & 73)> 0 ||(_board & 146) > 0 || (_board & 292) > 0) 1
    else if ((_board & 37376)> 0 ||(_board & 74752) > 0 || (_board & 149504) > 0) 2
    else 0
  }

  override def gameEnded(): Boolean = freePositions == 0 || checkWin()

  protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || scoreRows > 0 || scoreCols > 0
  }

  final protected def score2players(player: Byte): Int = {
    player match {
      case 2 => -1
      case 1 => 1
      case _ => ??? // could be zero, but should never reach here.
    }
  }

  override def score(): Score = {
    if (checkWin()) score2players(_lastPlayer)
    else 0
  }

  override protected def board(pos: (Short, Short)): Player = ???

  override protected def board(pos: (Short, Short))(p: Player): Unit = ???

  override protected def generateMoves(): IndexedSeq[(Short, Short)] = ???
}
