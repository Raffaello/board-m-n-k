package game

/**
  * @todo it could be generalized for p players....
  * @param m number of rows
  * @param n number of cols
  * @param k number of same move of a player "in a row" (or col or diagonal)
  */
class BoardMNK(m: Short, n: Short, k: Short) extends BoardMNKP(m, n, k, 2) {
//  def score(): Int = {
//    val score1 = score(1)
//    val score2 = score(2)
//
//    // score should be 0 or 1
//    assert(score1 < 2 && score2 < 2)
//    if (score2 > score1) {
//      return -score2
//    }
//
//    score1
//  }
}
