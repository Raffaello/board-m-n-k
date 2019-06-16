package bench.benchmarks

import ai.cakes.AiBoard
import game.{BoardTicTacToe, Score}
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object TicTacToeSolvers {
  val board: Gen[String] = Gen.single("BoardTicTacToe")("g")

  def aiBoardSolver(board: AiBoard): Score = board.solve

  def alphaBetaInt(board: BoardTicTacToe): Score = Math.round(ai.cakes.alphaBeta(board)).toInt
}
