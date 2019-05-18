package bench.benchmarks

import ai.AiBoard
import game.{BoardTicTacToe, Score}
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object TicTacToeSolvers {
  val ticTacToe1: Gen[String] = Gen.single("BoardTicTacToe")("t1")
  val ticTacToe2: Gen[String] = Gen.single("BoardTicTacToe2")("t2")

  def aiBoardSolver(board: AiBoard): Score = board.solve

  def alphaBeta(board: BoardTicTacToe): Score = Math.round(ai.alphaBeta(board)).toInt
}
