package game

import cats.implicits._
import game.Implicit.convertToPlayer
import game.boards.implementations.{Board1dArray, Board2dArray, BoardBitBoard}
import game.boards.{BoardDepthAware, BoardMN, BoardPlayerScore}
import game.types._

abstract class BoardMNKP(m: Short, n: Short, val k: Short, val numPlayers: Player) extends BoardMN(m, n)
  with BoardDepthAware with BoardPlayerScore {

  require(k <= m || k <= n)
  require(numPlayers >= 2)

  def playMove(position: Position, player: Player): Boolean = {
    assert(player >= 1 && player <= numPlayers)
    if (boardPlayer(position) > 0) false
    else {
      boardPlayer_=(position)(player)
      freePositions -= 1
      _depth += 1
      _lastMove = position
      _lastPlayer = player
      true
    }
  }

  def undoMove(position: Position, player: Player): Boolean = {
    if (boardPlayer(position) === player) {
      boardPlayer_=(position)(0)
      freePositions += 1
      _depth -= 1
      true
    } else false
  }

  // last player win? 1
  // last player lost? -1
  // else 0
//  def score(): Score = checkScore(_lastPlayer)


  override def score(player: Player): Score = checkScore(player)

//  def score(): Score = score(_lastPlayer)

  // check if board has k in a row of any player then return true otherwise false
  // create a general method to check player p if won.
  //  override protected def checkWin(): Boolean = {
  //    // trivial implementation
  //
  //    // check rows
  //    var count: Short = 0
  //
  //    for {
  //      p <- 1 until numPlayers
  //      i <- mIndices
  //    } {
  //      for (j <- nIndices) {
  //        if (boardPlayer(Position(i, j)) === p) count += 1
  //        else count = 0
  //
  //        if (count === k) return true
  //      }
  //    }
  //
  //    // check cols
  //    count = 0
  //    for {
  //      p <- 1 until numPlayers
  //      j <- nIndices
  //    } {
  //     for (i <- mIndices) {
  //       if (boardPlayer(Position(i, j)) === p) count += 1
  //       else count = 0
  //
  //       if (count === k) return true
  //     }
  //    }
  //
  //    // check diag SE
  //
  //    // check diag NE
  //    for {
  //      p <- 1 until numPlayers
  //      i <- mIndices
  //      j <- nIndices
  //      if scoreDiagNE(p, Position(i, j)) > 0
  //    } return true
  //
  //    // else
  //    false
  //  }

  override def gameEnded(): Boolean = gameEnded(depth)

  def gameEnded(depth: Int): Boolean = {
    if (depth < minWinDepth) false
    else if (freePositions === 0) true
    else checkWin()
  }

  def opponent(player: Player): Player = {
    assert(player >= 1 && player <= numPlayers)
    (player % numPlayers) + 1
  }

  def nextPlayer(): Player = opponent(lastPlayer)
}

object BoardMNKP {
  def apply(m: Short, n: Short, k: Short, numPlayers: Player, boardType: BoardMNTypeEnum): BoardMNKP = {
    boardType match {
      case BOARD_1D_ARRAY => new BoardMNKP(m, n, k, numPlayers) with Board1dArray
      case BOARD_2D_ARRAY => new BoardMNKP(m, n, k, numPlayers) with Board2dArray
      case BOARD_BIT_BOARD => new BoardMNKP(m, n, k, numPlayers) with BoardBitBoard
    }
  }
}
