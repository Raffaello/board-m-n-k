package game.boards.lookups

import game.BitBoard
import game.types.Position

import scala.annotation.tailrec

trait BitBoardLookup extends BitBoardValueLookup {
  lazy val bitsLookup: Map[Position, BitBoard] = {
    //    var acc: Map[Position, BitBoard] = Map.empty
    //    for {
    //      row <- mIndices
    //      col <- nIndices
    //    } {
    //      acc += (Position(row, col) -> (1 << mLookups(row) + col))
    //    }
    //
    //    acc

    @tailrec
    def computeRow(row: Short, acc: Map[Position, BitBoard]): Map[Position, BitBoard] = {

      @tailrec
      def computeCol(col: Short, acc: Map[Position, BitBoard]): Map[Position, BitBoard] = {
        if (col < 0) acc
        else {
          val c: Short = (col - 1).toShort
          val p: Position = Position(row, c)
          computeCol(c, acc + (p -> bitValue(p)))
        }
      }

      if (row < 0) acc
      else {
        val r: Short = (row - 1).toShort
        computeRow(r, computeCol((n - 1).toShort, acc))
      }
    }

    computeRow((m - 1).toShort, Map.empty)
  }
}
