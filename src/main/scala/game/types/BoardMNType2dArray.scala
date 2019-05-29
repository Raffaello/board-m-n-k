package game.types

import game.Board2d

trait BoardMNType2dArray extends BoardMNType {
  /*private[this]*/ protected var _board: Board2d = Array.ofDim[Byte](m, n)

  override protected def board: Board2d = _board
}
