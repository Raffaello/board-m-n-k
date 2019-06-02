package bench.benchmarks

import ai._
import ai.old.BoardMNKwithGetBoard
import game.{BoardMNK, Score}
import org.scalameter.api.Gen

object MNKSolvers {

  val ms: Gen[Int] = Gen.range("m")(3, 3, 1)
  val ns: Gen[Int] = Gen.range("n")(3, 3, 1)

  def boardSizes(k: Short): Gen[BoardMNK] = {
    for {
      n <- ns
      m <- ms
    } yield new BoardMNK(m.toShort, n.toShort, k)
  }

  val boardSizes: Gen[(Int, Int)] = {
    for {
      n <- ns
      m <- ms
    } yield (m, n)
  }

  def oldMinimax(m: Int, n: Int, k: Int): Score = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort)
    Stats.totalCalls = 0
    ai.old.minimax(board, true)
  }

  def traitMinimax(m: Int, n: Int, k: Int): MiniMax = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with MiniMax
    board
  }

  def traitMinimaxRaw(m: Int, n: Int, k: Int): MiniMaxRaw = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with MiniMaxRaw
    board
  }

  def oldNegamax(m: Int, n: Int, k: Int): Score = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort)
    Stats.totalCalls = 0
    ai.old.negamax(board, 1)
  }


  def traitNegamax(m: Int, n: Int, k: Int): NegaMax = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with NegaMax
    board
  }

  def alphaBeta(m: Int, n: Int, k: Int): Score = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort)
    Stats.totalCalls = 0
    Math.round(ai.alphaBeta(board)).toInt
  }

  def traitAlphaBeta(m: Int, n: Int, k: Int): AlphaBeta = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with AlphaBeta
    board
  }

  def oldAlphaBetaTTOld(m: Int, n: Int, k: Int): (Double, Int) = {
    val board = new BoardMNKwithGetBoard(m.toShort, n.toShort, k.toShort) with old.TranspositionTable
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val t = ai.old.alphaBetaWithMem(board, board)
    (t.score, board.transpositions.size)
  }

  def alphaBetaWithMem(m: Int, n: Int, k: Int): (Double, Int) = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with TranspositionTable
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val t = ai.alphaBetaWithMem(board, board)
    (t.score, board.transpositions.size)
  }

  def traitAlphaBetaTT(m: Int, n: Int, k: Int): AlphaBetaTransposition = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with AlphaBetaTransposition
    board
  }
}
