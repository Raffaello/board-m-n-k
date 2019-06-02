package game

import cats.implicits._
import game.types.Position

class BoardMNKPLookUp(m: Short, n: Short, k: Short, p: Byte) extends BoardMNKP(m, n, k, p) {

  /**
    * TODO, refactor with a trait? (no parameter allowed yet)
    * TODO also should be protected? test will be different
    * TODO review....
    *
    * TODO all arrays must be cloned so at the moment using var :/
    */
  class CLookUps extends Cloneable {

    var rows: Array[Array[Byte]] = Array.ofDim[Byte](m, numPlayers)
    var cols: Array[Array[Byte]] = Array.ofDim[Byte](n, numPlayers)
    var lastPlayerIdx: Int = 0
    var ended: Option[Boolean] = Some(false)

    def inc(pos: Position, playerIdx: Int): Unit = {
      val (x, y) = (pos.row, pos.col)

      lastPlayerIdx = playerIdx
      rows(x)(playerIdx) = (1 + rows(x)(playerIdx)).toByte
      assert(rows(x)(playerIdx) <= n)

      cols(y)(playerIdx) = (1 + cols(y)(playerIdx)).toByte
      //      assert(cols(y)(playerIdx) <= m, s"${cols(y)(playerIdx)} -- $playerIdx, $pos -- ${_board.flatten.mkString}")
      // TODO DIAG1 and DIAG2

    }

    def dec(pos: Position, playerIdx: Int): Unit = {
      val (x, y) = (pos.row, pos.col)

      rows(x)(playerIdx) = (rows(x)(playerIdx) - 1).toByte
      assert(rows(x)(playerIdx) >= 0)

      cols(y)(playerIdx) = (cols(y)(playerIdx) - 1).toByte
      assert(cols(y)(playerIdx) >= 0, s"${cols(y)(playerIdx)} -- $playerIdx, $pos")
    }

    override def clone(): CLookUps = {
      val clone = super.clone().asInstanceOf[CLookUps]

      clone.rows = this.rows.map(_.clone())
      clone.cols = this.cols.map(_.clone())
      clone.lastPlayerIdx = lastPlayerIdx
      clone.ended = ended

      clone
    }
  }

  protected var _lookUps = new CLookUps

  def lookUps: CLookUps = _lookUps

  override def playMove(position: Position, player: Byte): Boolean = {
    lookUps.ended = None
    val res = super.playMove(position, player)
    if (res) {
      lookUps.inc(position, player - 1)
    }
    res
  }

  override def undoMove(position: Position, player: Byte): Boolean = {
    lookUps.dec(position, player - 1)
    lookUps.ended = None
    super.undoMove(position, player)
  }

  // Todo review this method and lookUps.ended ???
  override def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }
}
