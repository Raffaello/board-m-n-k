package benchmarks

object ArrayClone1d extends App {

  val m = 5000
  val n = 5000
  val mn = m * n
  val arr = Array.ofDim[Int](mn)

  def populateArray1d(): Unit = {
    var k = 1
    for {
      i <- 0 until mn
    } {
      arr(i) = k
      k += 1
    }
  }

  def clone1D(): Unit = {
    val t1 = System.currentTimeMillis()
    val b = arr.clone()
    val t2 = System.currentTimeMillis()
//    println(s"clone: ${t2 - t1}ms")
  }

  def arrayCopy1d(): Unit = {
    val t1 = System.currentTimeMillis()
    val b = Array.ofDim[Int](mn)
    Array.copy(arr, 0, b, 0, mn)
    val t2 = System.currentTimeMillis()
//    println(s"copy: ${t2 - t1}ms")
  }

  def forLoop1d(): Unit = {
    val d = Array.ofDim[Int](mn)
    val t1 = System.currentTimeMillis()
    for {
      i <- arr.indices
    } {
      d(i) = arr(i)
    }
    val t2 = System.currentTimeMillis()
//    println(s"for : ${t2 - t1}")
  }


  populateArray1d()
  val t = System.currentTimeMillis()
  val k = 500
  for (k <- 0 until k) {
//    forLoop1d() // 81ms ???
//    arrayCopy1d() // 27ms
    clone1D()  // 25ms
  }

  val t2 = System.currentTimeMillis()
  println(s"AVG: ${(t2 - t) / k.toDouble}ms")
}
