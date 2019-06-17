package cakes.game.boards.lookups

import cakes.game.types.BoardMNType

trait MNIndexLookup extends BoardMNType {
  lazy val mnIndices: Range = 0 to mn
}
