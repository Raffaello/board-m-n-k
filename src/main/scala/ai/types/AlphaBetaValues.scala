package ai.types
import game.Score

import Ordering.Implicits._

final case class AlphaBetaValues[T: Numeric](alpha: T, beta: T) {
  @inline
  def isAlphaGteBeta: Boolean = alpha >= beta
}

// TODO score cannot be used instead of Int.... is ambiguos.
object AlphaBetaValues {
  lazy val alphaBetaValueScore: AlphaBetaValues[Score] = AlphaBetaValues[Score](Int.MinValue, Int.MaxValue)
}
