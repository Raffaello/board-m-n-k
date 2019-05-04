package ai.mcts.old

object UCT {
  final val c: Double = Math.sqrt(2.0)

  def UCT(w: Double, n: Int, N: Int): Double = {
    val res = n match {
      case 0 => Double.MaxValue
      case _ => w / n.toDouble + c * Math.sqrt(Math.log(N) / n.toDouble)
    }
    assert(N != 0)
    assert(!res.isNaN)
    assert(!res.isInfinity)
    res
  }

  def findBestNode(node: Node): Node = {
    // TODO: UCT can be cached in the node and invalidated/updated in backpropagation.
    node.children.maxBy(c => UCT(c.state.stateScore, c.state.visitCount, node.state.visitCount))
  }
}
