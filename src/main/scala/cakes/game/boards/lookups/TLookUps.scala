package cakes.game.boards.lookups

import cakes.game.Implicit.convertToPlayer
import cakes.game.Player
import cakes.game.boards.BoardPlayers
import cakes.game.types.{BoardMNType, Position}

// TODO this file is strictly designed for a 2d array board implementation not for a general one, need to be rewritten

trait TLookUps extends BoardMNType with BoardPlayers {
  protected var _lookUps = new CLookUps

  // TODO remove/refactor, only used for testing.
  def lookUps: CLookUps = _lookUps

  /**
    * TODO also should be a concrete implementation for the different kind of boards. this is for 2d array
    * TODO rethink in a more general way.
    *
    * TODO the ended variable is not really significative:
    * check win once only for each playMove so it is just overhead the ended.
    * it is used because it might call in multiple times the check as a cache,
    * double checked it
    * ideally this internal states of rows and cols and lastplayers
    * should be encapsulated and just the ended returned true/false
    * so it means the LookUps must known the rules m,n,k,p .....
    * and call gameEnded() / checkWin(), otherwise it is just a data structure and it is ok.
    */
  final class CLookUps(
                        val rows: Array[Array[Player]] = Array.ofDim[Player](m, numPlayers),
                        val cols: Array[Array[Player]] = Array.ofDim[Player](n, numPlayers),
                        var lastPlayerIdx: Int = numPlayers - 1,
                        var ended: Option[Boolean] = Some(false)) {

    def deepCopy(
                  newRows: Array[Array[Player]] = rows,
                  newCols: Array[Array[Player]] = cols,
                  newLastPlayerIdx: Int = lastPlayerIdx,
                  newEnded: Option[Boolean] = ended
                ): CLookUps = {

      def copying(newArr: Array[Array[Player]], arr: Array[Array[Player]]): Array[Array[Player]] = {
        if (newArr eq arr) newArr.map(_.clone()) else newArr
      }

      def rowsCopy(): Array[Array[Player]] = copying(newRows, rows)

      def colsCopy(): Array[Array[Player]] = copying(newCols, cols)

      new CLookUps(rowsCopy(), colsCopy(), newLastPlayerIdx, newEnded)
    }

    def inc(pos: Position, playerIdx: Int): Unit = {
      ended = None // reset, force to check
      lastPlayerIdx = playerIdx

      rows(pos.row)(playerIdx) = 1 + rows(pos.row)(playerIdx)
      assert(rows(pos.row)(playerIdx) <= n)

      cols(pos.col)(playerIdx) = 1 + cols(pos.col)(playerIdx)
      assert(cols(pos.col)(playerIdx) <= m, s"${cols(pos.col)(playerIdx)} -- $playerIdx, $pos")
      // TODO DIAG1 and DIAG2
    }

    def dec(pos: Position, playerIdx: Int): Unit = {
      ended = None
      lastPlayerIdx = -1

      rows(pos.row)(playerIdx) = rows(pos.row)(playerIdx) - 1
      assert(rows(pos.row)(playerIdx) >= 0)

      cols(pos.col)(playerIdx) = cols(pos.col)(playerIdx) - 1
      assert(cols(pos.col)(playerIdx) >= 0, s"${cols(pos.col)(playerIdx)} -- $playerIdx, $pos")
    }
  }

}
