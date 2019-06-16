package ai.cakes

import ai.cakes.types.AlphaBetaValues
import game.Score

final case class Transposition(score: Score, depth: Int, ab: AlphaBetaValues[Score], isMaximizing: Boolean)
