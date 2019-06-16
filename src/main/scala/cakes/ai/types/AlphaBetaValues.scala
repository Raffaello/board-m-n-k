package cakes.ai.types
import game.Score

import Ordering.Implicits._

final case class AlphaBetaValues[T: Numeric](alpha: T, beta: T) {
  @inline
  def isAlphaGteBeta: Boolean = alpha >= beta
}

object AlphaBetaValues {
  lazy val alphaBetaValueScore: AlphaBetaValues[Score] = AlphaBetaValues(Int.MinValue, Int.MaxValue)
  lazy val alphaBetaValueInt: AlphaBetaValues[Int] = AlphaBetaValues(Int.MinValue, Int.MaxValue)
  lazy val alphaBetaValueDouble: AlphaBetaValues[Double] = AlphaBetaValues(Double.MinValue, Double.MaxValue)
}
