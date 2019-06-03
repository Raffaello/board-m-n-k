package game.boards.lookups

import game.BitBoard
import game.types.{BoardMNType, Position}


trait BitBoardValueLookup extends BoardMNType with MLookup {

  @inline
  protected def bitValue(position: Position): BitBoard = 1 << mLookups(position.row) + position.col
}

