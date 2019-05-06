package ai.mcts

import scala.collection.mutable.ListBuffer

package object tree {
  type Children = ListBuffer[Node]
}
