package ai

import game.Score

final case class Transposition(score: Score, depth: Int, ab: AB[Score], isMaximizing: Boolean)
