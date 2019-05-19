package bench

import ai.old.WithGetBoard
import ai.{alphaBeta => _, _}
import bench.benchmarks.TicTacToeSolvers._
import game.{BoardTicTacToe, BoardTicTacToe2, Score}
import org.scalameter.api._

object TicTacToeSolversBench extends Bench.OfflineRegressionReport {
  var score: Score = 0
  var totalCalls: Int = 0
  var cacheHits: Int = 0
  var cacheSize: Int = 0

  def reset(): Unit = {
    score = 0
    totalCalls = 0
    cacheHits = 0
    cacheSize = 0
  }

  performance of "TicTacToeSolversBench" config(
    exec.benchRuns -> 9,
    exec.minWarmupRuns -> 3,
    exec.maxWarmupRuns -> 3,
    exec.warmupCovThreshold -> 0.3,
    exec.independentSamples -> 3,
    exec.requireGC -> true,
    exec.outliers.retries -> 1,
    exec.outliers.suspectPercent -> 10,
    exec.outliers.covMultiplier -> 2.0,
    exec.reinstantiation.fullGC -> true,
    exec.reinstantiation.frequency -> 1
  ) in {
    performance of "TicTacToe" in {
      measure method "old.minimax" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = ai.old.minimax(new BoardTicTacToe, true)
        }
      }
      measure method "traitMinimax" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe() with MiniMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method "traitMinimaxRaw" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with MiniMaxRaw
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method "old.negamax" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = ai.old.negamax(new BoardTicTacToe(), 1)
        }
      }
      measure method "traitNegamax" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with NegaMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"alphaBeta" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = alphaBeta(new BoardTicTacToe)
        }
      }
      measure method s"traitAlphaBeta" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with AlphaBeta
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"oldAlphaBetaTTOld" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe with WithGetBoard with old.TranspositionTable
            val t = old.alphaBetaWithMem(board, board)
            score = Math.round(t.score).toInt
            cacheSize = board.transpositions.size
        }
      }
      measure method s"alphaBetaWithMem" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe with TranspositionTable
            val t = ai.alphaBetaWithMem(board, board)
            score = Math.round(t.score)
            cacheSize = board.transpositions.size
        }
      }
      measure method s"traitAlphaBetaTT" in {
        using(ticTacToe1) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls --- cacheHits: $cacheHits --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBetaTransposition
            val t = board.solve()
            score = t.score
            totalCalls = board.Stats.totalCalls
            cacheHits = board.Stats.cacheHits
            cacheSize = board.transpositions.size
        }
      }
    }

    performance of "TicTacToe2" in {
      measure method s"old.minimax" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = ai.old.minimax(new BoardTicTacToe2, true)
        }
      }
      measure method s"traitMinimax" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with MiniMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"traitMinimaxRaw" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with MiniMaxRaw
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"old.negamax" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = ai.old.negamax(new BoardTicTacToe2(), 1)
        }
      }
      measure method s"traitNegamax" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with NegaMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"alphaBeta" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = alphaBeta(new BoardTicTacToe2)
        }
      }
      measure method s"traitAlphaBeta" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBeta
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"oldAlphaBetaTTOld" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with WithGetBoard with old.TranspositionTable
            val t = old.alphaBetaWithMem(board, board)
            score = Math.round(t.score).toInt
            cacheSize = board.transpositions.size
        }
      }
      measure method s"alphaBetaWithMem" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with TranspositionTable
            val t = ai.alphaBetaWithMem(board, board)
            score = Math.round(t.score)
            cacheSize = board.transpositions.size
        }
      }
      measure method s"traitAlphaBetaTT" in {
        using(ticTacToe2) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls --- cacheHits: $cacheHits --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBetaTransposition
            val t = board.solve()
            score = t.score
            totalCalls = board.Stats.totalCalls
            cacheHits = board.Stats.cacheHits
            cacheSize = board.transpositions.size
        }
      }
    }

    performance of "TicTacToeArray1dBoard" in {
      measure method s"alphaBeta" in {
//        using(ticTacToe1) beforeTests reset afterTests {
//          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
//        } in {
//          _ => score = alphaBeta(new BoardTicTacToe)
//        }
      }
    }

    performance of "TicTacToeBitSetBoard" in {
      measure method s"alphaBeta" in {
//        using(ticTacToe1) beforeTests reset afterTests {
//          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
//        } in {
//          _ => score = alphaBeta(new BoardTicTacToe)
//        }
      }
    }

    performance of "TicTacToeBitBoard" in {
      measure method s"alphaBeta" in {
//        using(ticTacToe1) beforeTests reset afterTests {
//          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
//        } in {
//          _ => score = alphaBeta(new BoardTicTacToe)
//        }
      }
    }
  }
}


