package cakes.benchmarks

import org.scalameter.api.Gen

object Array1dCopy {

  val ns: Gen[Int] = Gen.range("n")(3, 5, 1)

  val arrays1d: Gen[(Int, Array[Int])] = for {
    n <- ns
  } yield {
    val n2 = n * n
    val a = Array.ofDim[Int](n2)
    (n2, populateArray1d(n, a))
  }

  def populateArray1d(n: Int, a: Array[Int]): Array[Int] = {
    for {i <- 0 until n} a(i) = i + 1
    a
  }

  def clone1d(a: Array[Int]): Array[Int] = a.clone()

  def array1dCopy(n: Int, a: Array[Int]): Array[Int] = {
    val b = Array.ofDim[Int](n)
    Array.copy(a, 0, b, 0, n)
    b
  }

  def forLoop1d(n: Int, a: Array[Int]): Array[Int] = {
    val d = Array.ofDim[Int](n)
    for (i <- a.indices) d(i) = a(i)
    d
  }
}
