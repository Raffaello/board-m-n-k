package ai

import game.Score

trait AlphaBetaNextMove {
  protected var _alphaBetaNextMove: AB[Score] = (Int.MinValue, Int.MaxValue)

  def alphaBetaNextMove: AB[Score] = _alphaBetaNextMove

}
