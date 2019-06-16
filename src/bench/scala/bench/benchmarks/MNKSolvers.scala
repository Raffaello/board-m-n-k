package bench.benchmarks

import cakes.ai._
import cakes.ai.old.BoardMNKwithGetBoard
import game.boards.implementations.Board2dArray
import game.types.BOARD_2D_ARRAY
import game.{BoardMNK, Score}
import org.scalameter.api.Gen

object MNKSolvers {

  val ms: Gen[Int] = Gen.range("m")(3, 3, 1)
  val ns: Gen[Int] = Gen.range("n")(3, 3, 1)

  def boardSizes(k: Short): Gen[BoardMNK] = {
    for {
      n <- ns
      m <- ms
    } yield BoardMNK(m.toShort, n.toShort, k, BOARD_2D_ARRAY)
  }

  val boardSizes: Gen[(Int, Int)] = {
    for {
      n <- ns
      m <- ms
    } yield (m, n)
  }

  def oldMinimax(m: Int, n: Int, k: Int): Score = {
    val board = BoardMNK(m.toShort, n.toShort, k.toShort, BOARD_2D_ARRAY)
    Stats.totalCalls = 0
    old.minimax(board, true)
  }

  def traitMinimax(m: Int, n: Int, k: Int): MiniMax = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray with MiniMax
    board
  }

  def traitMinimaxRaw(m: Int, n: Int, k: Int): MiniMaxRaw = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray with MiniMaxRaw
    board
  }

  def oldNegamax(m: Int, n: Int, k: Int): Score = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray
    Stats.totalCalls = 0
    old.negamax(board, 1)
  }


  def traitNegamax(m: Int, n: Int, k: Int): NegaMax = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray with NegaMax
    board
  }

  def alphaBeta(m: Int, n: Int, k: Int): Score = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray
    Stats.totalCalls = 0
    Math.round(cakes.ai.alphaBeta(board)).toInt
  }

  def traitAlphaBeta(m: Int, n: Int, k: Int): AlphaBeta = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with Board2dArray  with AlphaBeta
    board
  }

  def oldAlphaBetaTTOld(m: Int, n: Int, k: Int): (Double, Int) = {
    val board = new BoardMNKwithGetBoard(m.toShort, n.toShort, k.toShort) with cakes.ai.old.TranspositionTable
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val t = old.alphaBetaWithMem(board, board)
    (t.score, board.transpositions.size)
  }

  def alphaBetaWithMem(m: Int, n: Int, k: Int): (Double, Int) = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with TranspositionTable with TranspositionTable2dArrayString
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val t = cakes.ai.alphaBetaWithMem(board, board)
    (t.score, board.transpositions.size)
  }

  def traitAlphaBetaTT(m: Int, n: Int, k: Int): AlphaBetaTransposition = {
    val board = new BoardMNK(m.toShort, n.toShort, k.toShort) with AlphaBetaTransposition with TranspositionTable2dArrayString
    board
  }
}
