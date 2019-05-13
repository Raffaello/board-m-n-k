import benchmarks.Array1dCopy._
import benchmarks.Array2dCopy._
import org.scalameter.api._

object ArrayCopyBench extends Bench.OfflineRegressionReport {

  performance of "ArrayCopyBench" config(
    exec.benchRuns -> 10,
    exec.minWarmupRuns -> 10,
    exec.maxWarmupRuns -> 10,
    exec.warmupCovThreshold -> 0.1,
    exec.independentSamples -> 10,
    exec.requireGC -> true,
    exec.outliers.retries -> 3,
    exec.outliers.suspectPercent -> 10,
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

      measure method "array2dCopy" in {
        using(arrays1d) in {
          case (n, a) => array1dCopy(n, a)
        }
      }

      measure method "forLoop2d" in {
        using(arrays1d) in {
          case (n, a) => forLoop1d(n, a)
        }
      }
    }
  }
}
