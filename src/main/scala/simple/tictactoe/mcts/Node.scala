package simple.tictactoe.mcts

import types.Position

import scala.collection.mutable.ListBuffer

class Node(val move: Option[Position], val parent: Option[Node], val state: GameState) {
  var wins: Double = 0.0
  var visits: Int = 0
  var untriedMoves = state.allRemainingMoves()
  var children: ListBuffer[Node] = new ListBuffer[Node]()
  val lastPlayer = state.lastPlayer

  def UCTSelectChild(): Node = {
    children.maxBy {
      case c if c.visits == 0 => Double.MaxValue
      case c => c.wins / c.visits.toDouble + Math.sqrt(2.0 * Math.log(this.visits.toDouble / c.visits.toDouble))
    }
  }

  def addChild(m: Option[Position], s: GameState): Node = {
    val n = new Node(m, Some(this), s)
    untriedMoves = untriedMoves.filterNot(m.contains(_))
    children += n
    n
  }

  def update(score: Double): Unit = {
    visits += 1
    wins += score
  }

  override def toString: String = {
    s"[LP: $lastPlayer | M: $move | W/V: $wins/$visits (${wins / visits})| U: $untriedMoves]\n"
  }

  def indentString(indent: Int): String = {
    val str = new StringBuffer()
    (0 until indent).foreach(_ => str.append("| "))
    str.toString
  }

  def treeToString(indent: Int): String = {
    val str = new StringBuffer()
    str.append(indentString(indent)).append(toString)
    children.foldLeft(str)((acc, c) => acc.append(c.treeToString(indent + 1)))
    str.toString
  }

  def childrenToString(): String = {
    val str = new StringBuffer()
    children.foldLeft(str.append(toString))((acc, c) => acc.append(indentString(1)).append(c.toString))
    str.toString
  }
}
