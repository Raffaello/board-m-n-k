package ai

import game.{Board, BoardMN, BoardMNK, BoardTicTacToe, Position}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

package object mcts {

  object UCT {
    final val c: Double = Math.sqrt(2.0)
    def UCT(w: Double, n: Int, t: Int): Double = {
      val res = (n, t) match {
        case (0,_) | (_, 0)  => Double.MaxValue
        case (_, _) => w / n.toDouble + c * Math.sqrt(Math.log(t) / n.toDouble)
      }
//      println(s"UCS = ${res}")
      assert(!res.isNaN)
      assert(!res.isInfinity)
      res
    }

    def findBestNode(node: Node): Node = {
      node.children.maxBy( c => UCT(c.state.stateScore, node.state.visitCount, c.state.visitCount ))
    }
  }

  class Node {
    var state: BoardState = _
    var parent: Node = null
    var children: ArrayBuffer[Node] = new ArrayBuffer[Node]()

    //    def UCT(): Double = w.toDouble / n.toDouble + c * Math.sqrt(Math.log(t) / n.toDouble)
    def randomChild(): Node = {
      val size = children.length
      size match {
        case 0 => this
        case _ => children(scala.util.Random.nextInt(children.length))
      }
    }
  }

   class BoardState(m: Short, n: Short) extends BoardTicTacToe with AiBoard {
//    override  var board: Board = _
    var player: Byte = _
    var visitCount: Int = 0
//    var wins: Int = 0
//    var loss: Int = 0
//    var drawn: Int = 0
    var stateScore: Double = 0.0


     def clone(state: BoardState) = {
       for {
         i <- mIndices
         j <- nIndices
         if state.board(i)(j) > 0
       } {
         this.board(i)(j) = state.board(i)(j)
       }


       this.depth = state.depth
       this.player = state.player
       this.visitCount = state.visitCount
//       this.wins = state.wins
//       this.loss = state.loss
//       this.drawn = state.drawn
       this.stateScore = state.stateScore
     }
     /**
       * TODO: refactor
       */
//     protected def generateMoves(): IndexedSeq[Position] = {
//       for {
//         i <- board.indices
//         j <- board(0).indices
//         if board(i)(j) == 0
//       } yield (i.toShort,j.toShort)
//     }

     /**
       * TODO: REFACTOR
       */
    def allPossibleStates(): List[BoardState] = {
      val states: ListBuffer[BoardState] = new ListBuffer[BoardState]()
      generateMoves().foreach{position =>
//        val (x, y) = position
        val newState = new BoardState(m,n)
        newState.clone(this)
//        newState.player = opponent()
        newState.playMove(position, newState.player)
//        newState.board(x)(y) = newState.player
        states += newState
      }

      states.toList
    }

    def randomMove(): Boolean = {
      val moves = generateMoves()
      if(moves.nonEmpty) {
        moves.lift(scala.util.Random.nextInt(moves.size)) match {
          case Some(p) => playMove(p, player)
          case None => false
        }
      } else false
    }

    def randomPlay(): Boolean = {
      // TODO
      false
    }

    // assuming player 1 or 2
    def opponent(): Byte = (3 - player).toByte
  }
}
