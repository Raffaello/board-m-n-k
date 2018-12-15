package game

/**
  * save only the ub and lb values, if this position is found than just cut-off all the sub-tree [at the moment]
  *
  * @param alpha value at game end status
  * @param beta  value at game end status
  */
case class Transposition(score: Double, depth: Int, alpha: Double, beta: Double, isMaximizing: Boolean)
