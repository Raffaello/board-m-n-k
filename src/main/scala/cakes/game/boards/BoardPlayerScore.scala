package cakes.game.boards

import cats.implicits._
import cakes.game.Implicit.{convertIntToShort, convertToPlayer}
import cakes.game.types.{BoardMNKPType, Position}
import cakes.game.{Player, Score}

import scala.annotation.tailrec

trait BoardPlayerScore extends BoardMNKPType with LastMoveTracker with BoardPlayers with GameBoard {

  /**
    * South-East direction checking: top-left to bottom-right
    */
  protected def scoreDiagSE(player: Player, position: Position): Player = {
    val bMin = Math.min(n - position.col, m - position.row)
    lazy val stopU = Math.min(k, bMin)
    lazy val stopD = position.min + 1

    @tailrec
    def foldDownRight(acc: Short, offset: Short): Short = {
      if (offset === stopU) acc
      else if (boardPlayer(Position(position.row + offset, position.col + offset)) === player)
        foldDownRight(acc + 1, offset + 1)
      else acc
    }

    @tailrec
    def foldUpLeft(acc: Short, offset: Short): Short = {
      if (offset === stopD) acc
      else if (boardPlayer(Position(position.row - offset, position.col - offset)) === player)
        foldUpLeft(acc + 1, offset + 1)
      else acc
    }

    if (position.min + bMin >= k1) {
      val countD = foldDownRight(0, 1)
      val countU = foldUpLeft(0, 1)
      if (countD + countU >= k1) player
      else 0
    } else 0
  }

  protected def scoreDiagSE(player: Player): Player = scoreDiagSE(player, _lastMove)

  protected def scoreDiagSE(): Player = scoreDiagSE(_lastPlayer)

  /**
    * North-East direction checking: bottom-left to top-right
    */
  protected def scoreDiagNE(player: Player, position: Position): Player = {
    @tailrec
    def foldUpRight(acc: Short, i: Short, j: Short, depth: Short): Short = {
      if (depth === 0 || i < 0 || j >= n || boardPlayer(Position(i, j)) =!= player) acc
      else foldUpRight(acc + 1, i - 1, j + 1, depth - 1)
    }

    @tailrec
    def foldDownLeft(acc: Short, i: Short, j: Short, depth: Short): Short = {
      if (depth === 0 || i >= m || j < 0 || boardPlayer(Position(i, j)) =!= player) acc
      else foldDownLeft(acc + 1, i + 1, j - 1, depth - 1)
    }

    //    if (m - i >= k && n - j >= k) {
    val countD = foldUpRight(0, position.row - 1, position.col + 1, k1)
    val countU = foldDownLeft(0, position.row + 1, position.col - 1, k1)

    if (countD + countU >= k1) player
    else 0
  }

  protected def scoreDiagNE(player: Player): Player = scoreDiagNE(player, _lastMove)

  protected def scoreDiagNE(): Player = scoreDiagNE(_lastPlayer)

  protected def scoreCol(player: Player, position: Position): Player = {
    @tailrec
    def foldDown(acc: Short, i: Short, stop: Short): Short = {
      if (i === stop) acc
      else if (boardPlayer(Position(i, position.col)) === player) foldDown(acc + 1, i + 1, stop)
      else acc
    }

    @tailrec
    def foldUp(acc: Short, i: Short, stop: Short): Short = {
      if (i < stop) acc
      else if (boardPlayer(Position(i, position.col)) === player) foldUp(acc + 1, i - 1, stop)
      else acc
    }


    val countD = foldDown(0, position.row + 1, Math.min(m, position.row + k))
    val countU = foldUp(0, position.row - 1, Math.max(0, position.row - k))

    if (countD + countU >= k1) player else 0
  }

  protected def scoreCol(player: Player): Player = scoreCol(player, _lastMove)

  protected def scoreCol(): Player = scoreCol(_lastPlayer)

  protected def scoreRow(player: Player, position: Position): Player = {
    @tailrec
    def foldRight(acc: Short, j: Short, stop: Short): Short = {
      if (j === stop) acc
      else if (boardPlayer(position.copy(col = j)) === player) foldRight(acc + 1, j + 1, stop)
      else acc
    }

    @tailrec
    def foldLeft(acc: Short, j: Short, stop: Short): Short = {
      if (j < stop) acc
      else if (boardPlayer(position.copy(col = j)) === player) foldLeft(acc + 1, j - 1, stop)
      else acc
    }

    //    if (lookUps.rows(i)(lookUps.lastPlayerIdx) >= k) {
    // possible win
    val countR = foldRight(0, position.col + 1, Math.min(n, position.col + k))
    val countL = foldLeft(0, position.col - 1, Math.max(0, position.col - k))

    if (countR + countL >= k1) player
    else 0
  }

  protected def scoreRow(player: Player): Player = scoreRow(player, _lastMove)

  protected def scoreRow(): Player = scoreRow(_lastPlayer)

  protected def checkScore(player: Player): Score = {
    def evaluate(score: Player): Option[Score] = {
      if (score =!= emptyCell) {
        if (_lastPlayer === player) Some(1)
        else Some(-1)
      } else None
    }

    evaluate(scoreRow())
      .getOrElse(evaluate(scoreCol())
        .getOrElse(evaluate(scoreDiagSE())
          .getOrElse(evaluate(scoreDiagNE())
            .getOrElse(0))))
  }

  override protected def checkWin(): Boolean = {
    scoreRow() =!= emptyCell ||
      scoreCol() =!= emptyCell ||
      scoreDiagNE() =!= emptyCell ||
      scoreDiagSE() =!= emptyCell
  }
}
