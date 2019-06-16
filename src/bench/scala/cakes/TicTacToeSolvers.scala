package cakes

import cakes.ai.old.GetBoard
import cakes.ai._
import cakes.benchmarks.TicTacToeSolvers._
import cakes.game._
import cakes.game.boards.implementations.Board2dArray
import cakes.game.types.BOARD_2D_ARRAY
import org.scalameter.api._


// TODO find a better method to test, have to test only solve method
// initialization and assignment of other variable has to been move out.
object TicTacToeSolvers extends Bench.OfflineRegressionReport {
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
    exec.benchRuns -> 25,
    exec.minWarmupRuns -> 5,
    exec.maxWarmupRuns -> 5,
    exec.warmupCovThreshold -> 0.3,
    exec.independentSamples -> 5,
    exec.requireGC -> true,
    exec.outliers.retries -> 1,
    exec.outliers.suspectPercent -> 10,
    exec.outliers.covMultiplier -> 2.0,
    exec.reinstantiation.fullGC -> true,
    exec.reinstantiation.frequency -> 5,
    reports.resultDir -> (reports.resultDir + this.getClass.getName)
  ) in {
    performance of "TicTacToe (lookups)" in {
      measure method "old.minimax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = old.minimax(BoardTicTacToe(BOARD_2D_ARRAY), true)
        }
      }
      measure method "traitMinimax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe() with Board2dArray with MiniMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method "traitMinimaxRaw" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with Board2dArray  with MiniMaxRaw
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method "old.negamax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = old.negamax(new BoardTicTacToe() with Board2dArray, 1)
        }
      }
      measure method "traitNegamax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with Board2dArray with NegaMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"alphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = alphaBetaInt(new BoardTicTacToe with Board2dArray)
        }
      }
      measure method s"traitAlphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe with Board2dArray with AlphaBeta
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"oldAlphaBetaTTOld" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe with Board2dArray with GetBoard with cakes.ai.old.TranspositionTable
            val t = old.alphaBetaWithMem(board, board)
            score = Math.round(t.score).toInt
            cacheSize = board.transpositions.size
        }
      }
      measure method s"alphaBetaWithMem" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe with TranspositionTable with TranspositionTable2dArrayString
            val t = alphaBetaWithMem(board, board)
            score = Math.round(t.score)
            cacheSize = board.transpositions.size
        }
      }
      measure method s"traitAlphaBetaTT" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls --- cacheHits: $cacheHits --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBetaTransposition with TranspositionTable2dArrayString
            val s = board.solve
            score = s
            totalCalls = board.Stats.totalCalls
            cacheHits = board.Stats.cacheHits
            cacheSize = board.transpositions.size
        }
      }
    }

    performance of "TicTacToe2 (specific checks)" in {
      measure method s"old.minimax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = old.minimax(new BoardTicTacToe2, true)
        }
      }
      measure method s"traitMinimax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with MiniMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"traitMinimaxRaw" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with MiniMaxRaw
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"old.negamax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = old.negamax(new BoardTicTacToe2(), 1)
        }
      }
      measure method s"traitNegamax" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with NegaMax
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"alphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = alphaBetaInt(new BoardTicTacToe2)
        }
      }
      measure method s"traitAlphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBeta
            score = aiBoardSolver(board)
            totalCalls = board.Stats.totalCalls
        }
      }
      measure method s"oldAlphaBetaTTOld" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with GetBoard with cakes.ai.old.TranspositionTable
            val t = old.alphaBetaWithMem(board, board)
            score = Math.round(t.score).toInt
            cacheSize = board.transpositions.size
        }
      }
      measure method s"alphaBetaWithMem" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with TranspositionTable with TranspositionTable2dArrayString
            val t = alphaBetaWithMem(board, board)
            score = Math.round(t.score)
            cacheSize = board.transpositions.size
        }
      }
      measure method s"traitAlphaBetaTT" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: $totalCalls --- cacheHits: $cacheHits --- cacheSize: $cacheSize")
        } in {
          _ =>
            val board = new BoardTicTacToe2 with AlphaBetaTransposition with TranspositionTable2dArrayString
            score = board.solve
            totalCalls = board.Stats.totalCalls
            cacheHits = board.Stats.cacheHits
            cacheSize = board.transpositions.size
        }
      }
    }

    performance of "TicTacToeArray1dBoard (dedicated)" in {
      measure method s"alphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = Math.round(alphaBeta(new BoardTicTacToe1dArray)).toInt
        }
      }
    }

    performance of "TicTacToeBitBoard (dedicated)" in {
      measure method s"alphaBeta" in {
        using(board) beforeTests reset afterTests {
          println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
        } in {
          _ => score = Math.round(alphaBeta(new BitBoardTicTacToe)).toInt
        }
      }
    }
  }
}


