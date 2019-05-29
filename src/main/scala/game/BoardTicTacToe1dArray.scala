package game

import game.boards.concrete.Board1dArray
import game.boards.{BoardDepthAware, BoardMN, BoardPlayers, LastMoveTracker}
import game.types.{BoardMNSize, Position}

/**
  * TODO: DRAFT extends BoardMNK
  */
class BoardTicTacToe1dArray extends BoardMN(BoardMNSize(3, 3)) with Board1dArray with LastMoveTracker with BoardDepthAware with BoardPlayers {
  override val numPlayers: Player = 2

  override def display(): String = ???

  protected val minWinDepth: Int = 5

  val m2 = m * 2

  // TODO not vey elegant this block of code... ??
  override def playMove(position: Position, player: Byte): Boolean = {
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
    if (boardPlayer(position) == player) {
      boardPlayer_=(position)(0)
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

  protected def scoreRow(row: Int): Int = {
    val i = mLookups(row)
    if (board(i) == board(i + 1) && board(i) == board(i + 2)) board(i)
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (board(col) == board(m + col) && board(col) == board(m2 + col)) board(col)
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (board(0) == board(m + 1) && board(0) == board(m2 + 2)) board(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (board(m2) == board(m + 1) && board(m2) == board(2)) board(2)
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
