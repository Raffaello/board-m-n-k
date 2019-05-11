package game

class BoardMNKPLookUp(m: Short, n: Short, k: Short, p: Byte) extends BoardMNKP(m, n, k, p) {

  /**
    * TODO, refactor with a trait? (no parameter allowed yet)
    * TODO also shuold be protected? test will be different
    * TODO review....
    *
    * TODO all arrays must be cloned so at the moment using var :/
    */
  class CLookUps extends Cloneable {

    var rows: Array[Array[Byte]] = Array.ofDim[Byte](m, numPlayers)
    var cols: Array[Array[Byte]] = Array.ofDim[Byte](n, numPlayers)
    var lastPlayerIdx: Int = 0
    // TODO: refactor won with ended (?)
    var won: Option[Boolean] = Some(false)

    def inc(pos: Position, playerIdx: Int): Unit = {
      lastPlayerIdx = playerIdx
      rows(pos._1)(playerIdx) = (1 + rows(pos._1)(playerIdx)).toByte
      assert(rows(pos._1)(playerIdx) <= n)
      cols(pos._2)(playerIdx) = (1 + cols(pos._2)(playerIdx)).toByte
      assert(cols(pos._2)(playerIdx) <= m, s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos -- ${_board.flatten.mkString}")
      // TODO DIAGS1 and DIAG2

    }

    def dec(pos: Position, playerIdx: Int): Unit = {
      rows(pos._1)(playerIdx) = (rows(pos._1)(playerIdx) - 1).toByte
      assert(rows(pos._1)(playerIdx) >= 0)
      cols(pos._2)(playerIdx) = (cols(pos._2)(playerIdx) - 1).toByte
      assert(cols(pos._2)(playerIdx) >= 0, s"${cols(pos._2)(playerIdx)} -- $playerIdx, $pos")
    }

    override def clone(): CLookUps = {
      val clone = super.clone().asInstanceOf[CLookUps]
      clone.rows = this.rows.map(_.clone())
      clone.cols = this.cols.map(_.clone())
      clone
    }
  }

  protected var _LookUps = new CLookUps
  def LookUps(): CLookUps = _LookUps

  override def playMove(position: Position, player: Byte): Boolean = {
    LookUps.won = None
    val res = super.playMove(position, player)
    if (res) {
      LookUps.inc(position, player - 1)
    }
    res
  }

  override def undoMove(position: Position, player: Byte): Boolean = {
    LookUps.dec(position, player - 1)
    LookUps.won = None
    super.undoMove(position, player)
  }

  /**
    * TODO: redesign the lookups and game ended checking and check win too....
    * TODO: at the moment it is not so performant and neither really clear, clean...
    */
  override def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) {
      LookUps.won = Some(false)
      false
    }
    else if (freePositions == 0) true
    else checkWin()
  }
}
