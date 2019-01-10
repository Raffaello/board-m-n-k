import game.BoardMNK
import ai._


object Main extends App {

  sealed trait _Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }

  sealed abstract class SolverBoard(m: Short, n: Short, k: Short) extends BoardMNK(m, n, k) {
    def result(): Any

    def stats(): _Stats
  }

  sealed abstract class SolverBoardRaw(m: Short, n: Short, k: Short) extends SolverBoard(m, n, k) {
    def stats(): _Stats = new _Stats {
      totalCalls = ai.Stats.totalCalls
      cacheHits = ai.Stats.cacheHits
    }
  }

  sealed abstract class SolverBoardAiTrait(m: Short, n: Short, k: Short) extends SolverBoard(m, n, k) with AiBoard {
    def stats() = new _Stats {
      totalCalls = Stats.totalCalls
      cacheHits = Stats.cacheHits
    }
  }

  sealed class MiniMaxRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result() = ai.minimax(this, true)
  }

  sealed class MiniMaxTraitRaw(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with MiniMax {
    def result() = this.solveRaw()
  }

  sealed class MiniMaxTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with MiniMax {
    def result() = this.solve()
  }

  sealed class NegaMaxRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result() = ai.negamax(this, 1)
  }

  sealed class NegaMaxTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with NegaMax {
    def result() = this.solve()
  }

  sealed class AlphaBetaRaw(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) {
    def result() = ai.alphaBeta(this)
  }

  sealed class AlphaBetaTTOld(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) with TranspositionTableOld {
    def result() = ai.alphaBetaWithMemOld(this, this)
  }

  sealed class AlphaBetaTT(m: Short, n: Short, k: Short) extends SolverBoardRaw(m, n, k) with TranspositionTable {
    def result() = ai.alphaBetaWithMem(this, this)
  }

  sealed class AlphaBetaTrait(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta {
    def result() = this.solve()
  }

  sealed class AlphaBetaTraitTTOld(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta with TranspositionTableOld {
    def result() = this.solve()
  }

  sealed class AlphaBetaTraitTT(m: Short, n: Short, k: Short) extends SolverBoardAiTrait(m, n, k) with AlphaBeta with TranspositionTable {
    def result() = this.solve()
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
  val choices = IndexedSeq[(String, SolverBoard)](
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
  val c = choices(choice)
  println(s"you choose: ${c._1}")


  println(s"Board size ${m}x${n}x$k")
  val startTime = System.currentTimeMillis()
  val r = c._2.result()
  val s = c._2.stats()
  val endTime = System.currentTimeMillis()
  println(s"Result: $r --- =[${c._2.getClass}]")
  println(s"Total Time: ${endTime - startTime}ms")
  println(s"Total calls: ${s.totalCalls}")
  println(s"Total cache: ${s.cacheHits}")

}
