package ai

import ai.types.AlphaBetaValues
import game.Score

final case class Transposition(score: Score, depth: Int, ab: AlphaBetaValues[Score], isMaximizing: Boolean)
