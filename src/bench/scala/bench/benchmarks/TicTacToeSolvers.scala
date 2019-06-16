package bench.benchmarks

import cakes.ai.{AiBoard, alphaBeta}
import cakes.game.{BoardTicTacToe, Score}
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object TicTacToeSolvers {
  val board: Gen[String] = Gen.single("BoardTicTacToe")("g")

  def aiBoardSolver(board: AiBoard): Score = board.solve

  def alphaBetaInt(board: BoardTicTacToe): Score = Math.round(alphaBeta(board)).toInt
}
