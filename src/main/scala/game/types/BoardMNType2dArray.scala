package game.types

import game.Board2d

private[game] trait BoardMNType2dArray extends BoardMNType {
  type Board = Board2d
  /*private[this]*/ protected var _board: Board = Array.ofDim[Byte](m, n)

  override protected def board: Board = _board
}
