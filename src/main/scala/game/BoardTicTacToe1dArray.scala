package game

import cats.implicits._
import game.Implicit.convertToPlayer
import game.boards.implementations.Board1dArray

class BoardTicTacToe1dArray extends BoardTicTacToe with Board1dArray {

  val m2 = m * 2

  protected def scoreRow(row: Int): Int = {
    val i = mLookups(row)
    if (board(i) === board(i + 1) && board(i) === board(i + 2)) board(i)
    else 0
  }

  // TODO generalized for k and p, move where appropriate
  protected def scoreRowKP(row: Short): Player = {
    val k = 3
    val i = mLookups(row)
    val head = board(i)

    if ((i + 1 until i + k).forall(v => head === board(v))) head
    else 0
  }

  protected def scoreCol(col: Short): Int = {
    if (board(col) === board(m + col) && board(col) === board(m2 + col)) board(col)
    else 0
  }

  // TODO generalized for k and p, move where appropriate
  protected def scoreCol2(col: Short): Player = {
    val k = 3
    val head: Player = board(col)

    if ((col + m until col + m * k).forall(v => head === v)) head
    else 0
  }

  protected def scoreDiagsTL(): Int = {
    if (board(0) === board(m + 1) && board(0) === board(m2 + 2)) board(0)
    else 0
  }

  protected def scoreDiagsBR(): Int = {
    if (board(m2) === board(m + 1) && board(m2) === board(2)) board(2)
    else 0
  }

  // TODO this should be removed
  override def gameEnded(): Boolean = freePositions === 0 || checkWin()

  // TODO this should be removed. (problems with lookups)
  override protected def checkWin(): Boolean = {
    scoreDiagsTL() > 0 || scoreDiagsBR() > 0 || mIndices.exists(i => scoreRow(i) > 0 || scoreCol(i) > 0)
  }
}
