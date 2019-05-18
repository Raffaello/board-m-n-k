package bench

import ai.Stats
import bench.benchmarks.MNKSolvers._
import game.Score
import org.scalameter.api.{Bench, exec}

object MNKSolversBench extends Bench.OfflineRegressionReport {

  var score: Score = 0
  var totalCalls: Int = 0
  var cacheHits: Int = 0
  var cacheSize: Int = 0

  performance of "MNKSolversBench" config(
    exec.benchRuns -> 15,
    exec.minWarmupRuns -> 3,
    exec.maxWarmupRuns -> 3,
    exec.warmupCovThreshold -> 0.3,
    exec.independentSamples -> 5,
    exec.requireGC -> true,
    exec.outliers.retries -> 1,
    exec.outliers.suspectPercent -> 10,
    exec.outliers.covMultiplier -> 2.0,
    exec.reinstantiation.fullGC -> true,
    exec.reinstantiation.frequency -> 1
  ) in {
    for (k <- 3 to 3) {
      performance of "MNKSolvers" in {
        measure method s"old.minimax_k=$k" in {
          using(boardSizes) afterTests {
            println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
          } in {
            case (m, n) => score = oldMinimax(m, n, k)
          }
        }

        measure method s"traitMinimax_k=$k" in {
          using(boardSizes) beforeTests {
            totalCalls = 0
          } afterTests {
            println(s"Score: $score --- totalCalls: $totalCalls")
          } in {
            case (m, n) =>
              val board = traitMinimax(m, n, k)
              score = board.solve
              totalCalls = board.Stats.totalCalls
          }
        }

        measure method s"traitMinimaxRaw_k=$k" in {
          using(boardSizes) beforeTests {
            totalCalls = 0
          } afterTests {
            println(s"Score: $score --- totalCalls: $totalCalls")
          } in {
            case (m, n) =>
              val board = traitMinimaxRaw(m, n, k)
              score = board.solve
              totalCalls = board.Stats.totalCalls
          }
        }

        measure method s"old.negamax_k=$k" in {
          using(boardSizes) afterTests {
            println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
          } in {
            case (m, n) => score = oldNegamax(m, n, k)
          }
        }

        measure method s"traitNegamax_k=$k" in {
          using(boardSizes) beforeTests {
            totalCalls = 0
          } afterTests {
            println(s"Score: $score --- totalCalls: $totalCalls")
          } in {
            case (m, n) =>
              val board = traitNegamax(m, n, k)
              score = board.solve()
              totalCalls = board.Stats.totalCalls
          }
        }

        measure method s"alphaBeta_k=$k" in {
          using(boardSizes) afterTests {
            println(s"Score: $score --- totalCalls: ${Stats.totalCalls}")
          } in {
            case (m, n) => score = alphaBeta(m, n, k)
          }
        }

        measure method s"traitAlphaBeta_k=$k" in {
          using(boardSizes) beforeTests {
            totalCalls = 0
          } afterTests {
            println(s"Score: $score --- totalCalls: $totalCalls")
          } in {
            case (m, n) =>
              val board = traitAlphaBeta(m, n, k)
              score = board.solve()
              totalCalls = board.Stats.totalCalls
          }
        }
      }

      performance of "MNKSolversWithMem" in {
        measure method s"oldAlphaBetaTTOld_k=$k" in {
          using(boardSizes) afterTests {
            println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
          } in {
            case (m, n) =>
              val (s, size) = oldAlphaBetaTTOld(m, n, k)
              score = Math.round(s).toInt
              cacheSize = size
          }
        }

        measure method s"alphaBetaWithMem=$k" in {
          using(boardSizes) afterTests {
            println(s"Score: $score --- totalCalls: ${Stats.totalCalls} --- cacheHits: ${Stats.cacheHits} --- cacheSize: $cacheSize")
          } in {
            case (m, n) =>
              val (s, size) = alphaBetaWithMem(m, n, k)
              score = Math.round(s).toInt
              cacheSize = size
          }
        }

        measure method s"traitAlphaBetaTT_k=$k" in {
          using(boardSizes) beforeTests {
            totalCalls = 0
          } afterTests {
            println(s"Score: $score --- totalCalls: $totalCalls --- cacheHits: $cacheHits --- cacheSize: $cacheSize")
          } in {
            case (m, n) =>
              val board = traitAlphaBetaTT(m, n, k)
              val t = board.solve()
              score = t.score
              totalCalls = board.Stats.totalCalls
              cacheHits = board.Stats.cacheHits
              cacheSize = board.transpositions.size
          }
        }
      }
    }
  }
}
