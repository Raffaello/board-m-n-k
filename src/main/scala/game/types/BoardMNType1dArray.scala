package game.types

import game.Board1d

trait BoardMNType1dArray extends BoardMNType {
  type Board = Board1d

  lazy private[this] val _board: Board = Array.ofDim[Byte](mn)


  override protected def board: Board = _board
}
