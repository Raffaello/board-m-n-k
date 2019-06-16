package cakes.game.boards.lookups

import cakes.game.types.BoardMNType

import scala.collection.immutable.NumericRange

trait MLookup extends BoardMNType {

  lazy val mLookups: NumericRange[Int] = NumericRange[Int](0, mn, n)
}
