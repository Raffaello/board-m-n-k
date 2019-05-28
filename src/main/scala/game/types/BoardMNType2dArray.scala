package game.types

import game.Board2d

trait BoardMNType2dArray extends BoardMNType {
  protected var _board: Board2d = Array.ofDim[Byte](m, n)
}
