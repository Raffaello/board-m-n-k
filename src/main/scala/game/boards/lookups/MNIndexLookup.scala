package game.boards.lookups

import game.types.BoardMNType

trait MNIndexLookup extends BoardMNType {
  lazy val mnIndices: Range = 0 to mn
}
