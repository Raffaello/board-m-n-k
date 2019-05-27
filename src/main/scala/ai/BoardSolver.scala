package ai

import ai.old.BoardMNKwithGetBoard
import game.BoardMNK

object BoardSolver extends App {

  sealed trait _Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  sealed trait SolverBoardTrait {
    def result(): Any

    def stats(): _Stats
  }

  sealed trait StatsWrapper {
    def stats(): _Stats = new _Stats {
      totalCalls = ai.Stats.totalCalls
      cacheHits = ai.Stats.cacheHits
    }
  }

  sealed abstract class SolverBoard(m: Short, n: Short, k: Short) extends BoardMNK(m, n, k) with SolverBoardTrait {}

  sealed abstract class SolverBoardOld(m: Short, n: Short, k: Short) extends BoardMNKwithGetBoard(m, n, k) with SolverBoardTrait {}

  sealed abstract class SolverBoardRaw(m: Short, n: Short, k: Short) extends SolverBoard(m, n, k) with StatsWrapper {}

  sealed abstract class SolverBoardOldRaw(m: Short, n: Short, k: Short) extends SolverBoardOld(m, n, k) with StatsWrapper {}

  sealed abstract class SolverBoardAiTrait(m: Short, n: Short, k: Short) extends SolverBoard(m, n, k) with AiBoard with StatsWrapper {}

  sealed class MiniMaxRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result(): Int = ai.old.minimax(this, isMaximizingPlayer = true)
  }

  sealed class MiniMaxTraitRaw(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with ai.MiniMaxRaw {
    def result(): Int = this.solve
  }

  sealed class MiniMaxTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with MiniMax {
    def result(): Int = this.solve
  }

  sealed class NegaMaxRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result(): Int = ai.old.negamax(this, 1)
  }

  sealed class NegaMaxTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with NegaMax {
    def result(): Int = this.solve
  }

  sealed class AlphaBetaRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result(): Double = ai.alphaBeta(this)
  }

  sealed class AlphaBetaTTOld(m: Short, n: Short, k: Short) extends SolverBoardOldRaw(m, n, k) with ai.old.TranspositionTable {
    def result(): old.Transposition = ai.old.alphaBetaWithMem(this, this)
  }

  sealed class AlphaBetaTT(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) with ai.TranspositionTable {
    def result(): Transposition = ai.alphaBetaWithMem(this, this)
  }

  sealed class AlphaBetaTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta {
    def result(): Int = this.solve
  }

  sealed class AlphaBetaTraitTTOld(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta with ai.old.TranspositionTable {
    def result(): Int = this.solve
  }

  sealed class AlphaBetaTraitTT(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta with TranspositionTable {
    def result(): Int = this.solve
  }

  println("choose board size: ")
  print("m = ")
  val m = scala.io.StdIn.readShort()
  print("n = ")
  val n = scala.io.StdIn.readShort()
  print("k = ")
  val k = scala.io.StdIn.readShort()

  assert(k <= Math.min(m, n))

  // label, function name, is Trait?
  val choices = IndexedSeq[(String, SolverBoardTrait)](
    ("MiniMax Raw", new MiniMaxRaw(m, n, k)),
    ("MiniMax Trait Raw", new MiniMaxTraitRaw(m, n, k)),
    ("MiniMax Trait", new MiniMaxTrait(m, n, k)),
    ("Negamax", new NegaMaxRaw(m, n, k)),
    ("Negamax Trait", new NegaMaxTrait(m, n, k)),
    ("AlphaBeta", new AlphaBetaRaw(m, n, k)),
    ("AlphaBeta with TT Old", new AlphaBetaTTOld(m, n, k)),
    ("AlphaBeta with TT", new AlphaBetaTT(m, n, k)),
    ("AlphaBeta Trait", new AlphaBetaTrait(m, n, k)),
    ("AlphaBeta Trait with TT Old", new AlphaBetaTraitTTOld(m, n, k)),
    ("AlphaBeta Trait with TT", new AlphaBetaTraitTT(m, n, k)),
  )

  println("Please select the algorithm:")
  println(choices.zipWithIndex.map(x => s"${x._2}. ${x._1._1}\n").mkString)
  print("choice: ")
  val choice = scala.io.StdIn.readInt()
  println()
  val (ck, cv) = choices(choice)
  println(s"you choose: $ck")

  println(s"Board size ${m}x${n}x$k")
  val startTime = System.currentTimeMillis()
  val r = cv.result()
  val s = cv.stats()
  val endTime = System.currentTimeMillis()
  println(s"Result: $r --- =[${cv.getClass}]")
  println(s"Total Time: ${endTime - startTime}ms")
  println(s"Total calls: ${s.totalCalls}")
  println(s"Total cache: ${s.cacheHits}")
}
