package cakes.ai

import cats.implicits._
import game.types.Status
import game.{BoardMNKP, Player, Score}

trait AiBoard extends BoardMNKP with AiStats {
  protected def signum(value: Boolean): Byte = if (value) +1 else -1

  final lazy val aiPlayer: Player = settings.Loader.Ai.player

  def solve: Score

  def nextMove: Status[Score]

  final protected def initValue(player: Player): Score = if (player === aiPlayer) Int.MinValue else Int.MaxValue

  final protected def cmp(maximizing: Boolean): (Int, Int) => Int = if (maximizing) Math.max else Math.min
}
