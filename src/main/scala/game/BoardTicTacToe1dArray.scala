package game

import game.boards.{BoardDepthAware, BoardMN, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO: DRAFT extends BoardMNKP
  */
class BoardTicTacToe1dArray extends BoardMN(BoardMNSize(3, 3)) with Board1dArray with LastMoveTracker with BoardDepthAware {

  protected val minWinDepth: Int = 5
  protected var freePositions: Int = m * n

  val m2 = m * 2

  protected var _lastPlayer: Byte = 2

  def lastPlayer: Byte = this._lastPlayer

  override def playMove(position: Position, player: Byte): Boolean = {
    val (row, col) = (position.row, position.col)
    if (_board(row * m + col) > 0) false
    else {
      _board(row * m + col) = player
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  override def undoMove(position: Position, player: Player): Boolean = {
    val (i, j) = (position.row, position.col)

    if (_board(i * m + j) == player) {
      _board(i * m + j) = 0
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

  def opponent(player: Player): Player = ???

  def nextPlayer(): Player = ???


  override def consumeMoves()(f: Position => Unit): Unit = ??? //generateMoves().foreach(f)

  protected def scoreRow(row: Int): Int = {
    val i = row * m
    if (_board(i) == _board(i + 1) && _board(i) == _board(i + 2)) _board(i)
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (_board(col) == _board(m + col) && _board(col) == _board(m2 + col)) _board(col)
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (_board(0) == _board(m + 1) && _board(0) == _board(m2 + 2)) _board(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (_board(m2) == _board(m + 1) && _board(m2) == _board(2)) _board(2)
    else 0
  }

  override def gameEnded(): Boolean = freePositions == 0 || checkWin()

  protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
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
