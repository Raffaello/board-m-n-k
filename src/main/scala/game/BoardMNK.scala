package game

import game.boards.BoardDisplay
import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.types._

/**
  * TODO: potentially split in BoardNMK and BoardMNKLookUp (traits)
  */
abstract class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) with BoardDisplay {

  final protected def score2players(player: Player): Int = {
    player match {
      case 2 => -1
      case 1 => 1
    }
  }

  /**
    * TODO better generalization: score()(player: Byte => score: Int) ???
    */
  def score(): Score = {
    if (checkWin()) score2players(_lastPlayer)
    else 0
  }

  // todo remove
  override def score(player: Player): Score = score()

  override def display(): String = {
    val str: StringBuilder = new StringBuilder()
    val newLine = sys.props("line.separator")
    val n1 = n - 1
    val value: IndexedSeq[Char] = IndexedSeq('_', 'X', 'O')
    for (i <- mIndices) {
      for (j <- 0 until n1) {
        str ++= s" ${value(boardPlayer(Position(i.toShort, j.toShort)))} |"
      }

      str ++= s" ${value(boardPlayer(Position(i.toShort, n1.toShort)))}" + newLine
    }

    str ++= newLine
    str.toString()
  }
}

object BoardMNK {
  def apply(m: Short, n: Short, k: Short, boardType: BoardMNTypeEnum): BoardMNK = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardMNK(m, n, k) with Board1dArray
      case BOARD_2D_ARRAY => new BoardMNK(m, n, k) with Board2dArray
      case BOARD_BIT_BOARD => new BoardMNK(m, n, k) with BoardBitBoard
    }
  }
}
