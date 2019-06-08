package game

import cats.implicits._
import game.boards.implementations.BoardBitBoard
import game.types.Position

class BitBoardTicTacToe extends BoardTicTacToe with BoardBitBoard {

  override def playMove(position: Position, player: Player): Boolean = {
    if (boardPlayer(position) > 0) false
    else {
      boardPlayer_=(position)(player)
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  override def undoMove(position: Position, player: Player): Boolean = {
    if (boardPlayer(position) > 0) {
      boardPlayer_=(position)(player)
      freePositions += 1
      _depth -= 1
      true
    } else false
  }

  protected def scoreDiagsTL(): Int = {
    // 1 0 0 | 0 1 0 | 0 0 1 || 1 0 0 | 0 1 0 | 0 0 1
    // 1 + 16 + 255 || 2^9 + 2^13 + 2^17
    //  273 p1  || 139776 p2
    if ((board(0) & 273) === 273) 1
    else if ((board(1) & 273) === 273) 2
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    // 0 0 1 | 0 1 0 | 1 0 0 || 0 0 1 | 0 1 0 | 1 0 0
    // 4 + 16 + 64 || 2^11 + 2^13 + 2^15
    //  84 p1  || 43008 p2
    if ((board(0) & 84) === 84) 1
    else if ((board(1) & 84) === 84) 2
    else 0
  }

  def scoreRows: Int = {
    // 1 1 1 || 1 1 1
    // 1+2+4 ||
    if ((board(0) & 7) === 7 || (board(0) & 56) === 56 || (board(0) & 448) === 448) 1
    else if ((board(1) & 7) === 7 || (board(1) & 56) === 56 || (board(1) & 448) === 448) 2
    else 0
    //    board.find(bitBoard => (bitBoard & bitBoard >> 1 & bitBoard >> 2) > 0).getOrElse(0)
  }

  def scoreCols: Int = {
    // 1 0 0 | 1 0 0 | 1 0 0
    // 0 1 0 | 0 1 0 | 0 1 0
    // 0 0 1 | 0 0 1 | 0 0 1
    // 0 0 0   0 0 0   0 0 0 1 0 0 1 0 0 1 0 0
    // 1 + 8 + 64 = 73
    if ((board(0) & 73) === 73 || (board(0) & 146) === 146 || (board(0) & 292) === 292) 1
    else if ((board(1) & 73) === 73 || (board(1) & 146) === 146 || (board(1) & 292) === 292) 2
    else 0
  }

  override def gameEnded(): Boolean = freePositions === 0 || checkWin()

  override protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || scoreRows > 0 || scoreCols > 0
  }
}
