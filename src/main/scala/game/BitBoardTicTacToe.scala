package game

import game.boards.{BoardDepthAware, BoardMN, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO should extend BoardMNK, but the code is not clean yet
  */
class BitBoardTicTacToe extends BoardMN(BoardMNSize(3, 3)) with BoardDepthAware with LastMoveTracker {

  override protected def board: AnyRef = ???

  override def generateMoves(): IndexedSeq[Position] = ???

  val numPlayers: Player = 2

  var _board: BitBoard = 0

  override protected def board(pos: Position): Player = ???

  override protected def board_=(pos: Position)(p: Player): Unit = ???

  protected var _lastPlayer: Byte = numPlayers

  def lastPlayer: Byte = this._lastPlayer

  protected val minWinDepth: Int = 5 //(2 * k) - 2 // 2*(k-1) // 2*k1 // zero-based depth require to subtract 1 extra more

  def toStringArray: String = {
    var str = ""
    for {
      i <- 0 until m
      j <- 0 until n
    } {
      val v1 = (_board & boardValue(Position(i.toShort, j.toShort), 1)) > 0
      val v2 = (_board & boardValue(Position(i.toShort, j.toShort), 2)) > 0
      //      assert((v1 && !v2) || (!v1 && v2))
      if (v1) str += "X"
      else if (v2) str += "O"
      else str += "_"
    }

    str
  }

  private[this] def boardValue(position: Position, player: Player): BitBoard = {
    assert(position.row >= 0 && position.row < 3)
    assert(position.col >= 0 && position.col < 3)
    (1 << (position.row * m + position.col)) << mn * (player - 1)
  }

  private[this] def boardValueCheck(position: Position): BitBoard = {
    assert(position.row >= 0 && position.row < 3)
    assert(position.col >= 0 && position.col < 3)

    var sum = 0
    for (p <- 1 to numPlayers) sum += boardValue(position, p.toByte)
    sum
  }

  override def playMove(position: Position, player: Player): Boolean = {
    val value: BitBoard = boardValue(position, player)

    if ((_board & boardValueCheck(position)) > 0) false
    else {
      _board ^= value
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  override def undoMove(position: Position, player: Player): Boolean = {
    val value: BitBoard = boardValue(position, player)

    if ((_board & boardValueCheck(position)) > 0) {
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

  protected def scoreDiagsTL(): Int = {
    // 1 0 0 | 0 1 0 | 0 0 1 || 1 0 0 | 0 1 0 | 0 0 1
    // 1 + 16 + 255 || 2^9 + 2^13 + 2^17
    //  273 p1  || 139776 p2
    if ((_board & 273) == 273) 1
    else if ((_board & 139776) == 139776) 2
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    // 0 0 1 | 0 1 0 | 1 0 0 || 0 0 1 | 0 1 0 | 1 0 0
    // 4 + 16 + 64 || 2^11 + 2^13 + 2^15
    //  84 p1  || 43008 p2
    if ((_board & 84) == 84) 1
    else if ((_board & 43008) == 43008) 2
    else 0
  }

  def scoreRows: Int = {
    // 1 1 1 || 1 1 1
    // 1+2+4 ||
    if ((_board & 7) == 7 || (_board & 56) == 56 || (_board & 448) == 448) 1
    else if ((_board & 3584) == 3584 || (_board & 28672) == 28672 || (_board & 229376) == 229376) 2
    else 0
  }

  def scoreCols: Int = {
    // 1 0 0 | 1 0 0 | 1 0 0
    // 0 1 0 | 0 1 0 | 0 1 0
    // 0 0 1 | 0 0 1 | 0 0 1
    // 0 0 0   0 0 0   0 0 0 1 0 0 1 0 0 1 0 0
    // 1 + 8 + 64 = 73
    if ((_board & 73) == 73 || (_board & 146) == 146 || (_board & 292) == 292) 1
    else if ((_board & 37376) == 37376 || (_board & 74752) == 74752 || (_board & 149504) == 149504) 2
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
}
