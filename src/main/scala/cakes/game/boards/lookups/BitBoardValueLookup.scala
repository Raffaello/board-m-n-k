package cakes.game.boards.lookups

import cakes.game.BitBoard
import cakes.game.types.{BoardMNType, Position}


trait BitBoardValueLookup extends BoardMNType with MLookup {

  @inline
  protected def bitValue(position: Position): BitBoard = 1 << mLookups(position.row) + position.col
}

