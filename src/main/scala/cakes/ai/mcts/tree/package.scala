package cakes.ai.mcts

import scala.collection.mutable.ListBuffer

package object tree {
  // TODO replace with an Option[IndexedSeq[Node]] could be more performant (benchmark too).
  type Children = ListBuffer[Node]
}
