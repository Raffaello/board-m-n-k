package ai.cakes

import ai.cakes.types.AlphaBetaValues
import game.Score

trait AlphaBetaNextMove {
  protected var _alphaBetaNextMove: AlphaBetaValues[Score] = AlphaBetaValues.alphaBetaValueScore

  def alphaBetaNextMove: AlphaBetaValues[Score] = _alphaBetaNextMove
}
