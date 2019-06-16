package cakes.ai

import cakes.ai.types.AlphaBetaValues
import game.Score

trait AlphaBetaNextMove {
  protected var _alphaBetaNextMove: AlphaBetaValues[Score] = AlphaBetaValues.alphaBetaValueScore

  def alphaBetaNextMove: AlphaBetaValues[Score] = _alphaBetaNextMove
}
