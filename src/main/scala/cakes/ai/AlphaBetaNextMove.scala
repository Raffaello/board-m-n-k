package cakes.ai

import cakes.ai.types.AlphaBetaValues
import cakes.game.Score

trait AlphaBetaNextMove {
  protected var _alphaBetaNextMove: AlphaBetaValues[Score] = AlphaBetaValues.alphaBetaValueScore

  def alphaBetaNextMove: AlphaBetaValues[Score] = _alphaBetaNextMove
}
