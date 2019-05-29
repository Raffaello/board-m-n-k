package game

import game.boards.implementation.BoardBitBoard
import game.boards.{BoardDepthAware, BoardMN, BoardPlayers, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO should extend BoardMNKP, but the code is not clean yet
  * TODO implement BoardBitBoard with numplayers... then refactor
  */
class BitBoardTicTacToe extends BoardMN(BoardMNSize(3, 3)) with BoardBitBoard with BoardDepthAware with LastMoveTracker with BoardPlayers {

  override val numPlayers: Player = 2

  protected val minWinDepth: Int = 5

  override def playMove(position: Position, player: Player): Boolean = {
    assert(numPlayers > 0)
    require(player >= 1 && player <= numPlayers)
    if (boardPlayer(position) > 0) false
    else {
      boardPlayer_=(position)((player - 1).toByte)
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  override def undoMove(position: Position, player: Player): Boolean = {
    if (boardPlayer(position) > 0) {
      boardPlayer_=(position)((player - 1).toByte)
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
    if ((board(0) & 273) == 273) 1
    else if ((board(1) & 273) == 273) 2
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    // 0 0 1 | 0 1 0 | 1 0 0 || 0 0 1 | 0 1 0 | 1 0 0
    // 4 + 16 + 64 || 2^11 + 2^13 + 2^15
    //  84 p1  || 43008 p2
    if ((board(0) & 84) == 84) 1
    else if ((board(1) & 84) == 84) 2
    else 0
  }

  def scoreRows: Int = {
    // 1 1 1 || 1 1 1
    // 1+2+4 ||
    if ((board(0) & 7) == 7 || (board(0) & 56) == 56 || (board(0) & 448) == 448) 1
    else if ((board(1) & 7) == 7 || (board(1) & 56) == 56 || (board(1) & 448) == 448) 2
    else 0
  }

  def scoreCols: Int = {
    // 1 0 0 | 1 0 0 | 1 0 0
    // 0 1 0 | 0 1 0 | 0 1 0
    // 0 0 1 | 0 0 1 | 0 0 1
    // 0 0 0   0 0 0   0 0 0 1 0 0 1 0 0 1 0 0
    // 1 + 8 + 64 = 73
    if ((board(0) & 73) == 73 || (board(0) & 146) == 146 || (board(0) & 292) == 292) 1
    else if ((board(1) & 73) == 73 || (board(1) & 146) == 146 || (board(1) & 292) == 292) 2
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
