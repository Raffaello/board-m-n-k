package cakes.game.boards

import cakes.game.Player

trait BoardDisplay2Players extends BoardDisplay {
  override private[boards] def valueDisplay(player: Player): Char = IndexedSeq('_', 'X', 'O')(player)
}
