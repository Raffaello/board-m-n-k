package bench

import bench.benchmarks.Array1dCopy.{array1dCopy, arrays1d, clone1d, forLoop1d}
import bench.benchmarks.Array2dCopy._
import org.scalameter.api.{Bench, exec}

object ArrayCopy extends Bench.OfflineRegressionReport {

  performance of "ArrayCopyBench" config(
    exec.benchRuns -> 500,
    exec.minWarmupRuns -> 10,
    exec.maxWarmupRuns -> 10,
    exec.warmupCovThreshold -> 0.5,
    exec.independentSamples -> 5,
    exec.requireGC -> true,
    exec.outliers.retries -> 3,
    exec.outliers.suspectPercent -> 15,
    exec.outliers.covMultiplier -> 2.0,
    exec.reinstantiation.fullGC -> true,
    exec.reinstantiation.frequency -> 3
  ) in {
    performance of "Array2dCopy" in {
      measure method "mapClone" in {
        using(arrays2d) in {
          case (_, _, a) => mapClone2d(a)
        }
      }

      measure method "forClone" in {
        using(arrays2d) in {
          case (m, n, a) => forClone2d(m, n, a)
        }
      }

      measure method "array2dCopy" in {
        using(arrays2d) in {
          case (m, n, a) => array2dCopy(m, n, a)
        }
      }

      measure method "forLoop2d" in {
        using(arrays2d) in {
          case (m, n, a) => forLoop2d(m, n, a)
        }
      }
    }

    performance of "Array1dCopy" in {
      measure method "clone" in {
        using(arrays1d) in {
          case (_, a) => clone1d(a)
        }
      }

      measure method "array1dCopy" in {
        using(arrays1d) in {
          case (n, a) => array1dCopy(n, a)
        }
      }

      measure method "forLoop1d" in {
        using(arrays1d) in {
          case (n, a) => forLoop1d(n, a)
        }
      }
    }
  }
}
