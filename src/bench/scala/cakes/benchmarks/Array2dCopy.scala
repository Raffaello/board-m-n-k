package cakes.benchmarks

import org.scalameter.api.Gen

object Array2dCopy {

  val ns: Gen[Int] = Gen.range("n")(3, 5, 1)
  val arrays2d: Gen[(Int, Int, Array[Array[Int]])] = for {
    //    m <- ms
    n <- ns
  } yield {
    val a = Array.ofDim[Int](n, n)
    (n, n, populateArray2d(n, n, a))
  }

  def populateArray2d(m: Int, n: Int, a: Array[Array[Int]]): Array[Array[Int]] = {
    var k = 1
    for {
      i <- 0 until m
      j <- 0 until n
    } {
      a(i)(j) = k
      k += 1
    }

    a
  }

  def mapClone2d(a: Array[Array[Int]]): Array[Array[Int]] = a.map(_.clone)

  def forClone2d(m: Int, n: Int, a: Array[Array[Int]]): Array[Array[Int]] = {
    val b = Array.ofDim[Int](m, n)
    for (i <- a.indices) b(i) = a(i).clone()
    b
  }

  def array2dCopy(m: Int, n: Int, a: Array[Array[Int]]): Array[Array[Int]] = {
    val b = Array.ofDim[Int](m, n)
    for (i <- a.indices) Array.copy(a(i), 0, b(i), 0, n)
    b
  }

  def forLoop2d(m: Int, n: Int, a: Array[Array[Int]]): Array[Array[Int]] = {
    val d = Array.ofDim[Int](m, n)
    for {
      i <- a.indices
      j <- 0 until n
    } d(i)(j) = a(i)(j)
    d
  }
}
