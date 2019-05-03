package benchmarks

object Array2dClone extends App {

  val m = 5000
  val n = 5000
  val a = Array.ofDim[Int](m,n)

  def populateArray(): Unit = {
    var k = 1
    for{
      i <- 0 until m
      j <- 0 until n
    } {
      a(i)(j) = k
      k += 1
    }
  }

  def mapClone(): Unit = {
    val t1 = System.currentTimeMillis()
    val b = a.map(_.clone)
    val t2 = System.currentTimeMillis()
    println(s"map.clone :${t2 - t1}ms")
  }

  def arrayCopy(): Unit = {
    val t1 = System.currentTimeMillis()
    val b = Array.ofDim[Int](m, n)
    for (i <- a.indices) {
      Array.copy(a(i), 0, b(i), 0, n)
    }
    val t2 = System.currentTimeMillis()
    println(s"array.copy: ${t2 - t1}")
  }

  def forLoop(): Unit = {
    val d = Array.ofDim[Int](m,n)
    val t1 = System.currentTimeMillis()
    for {
      i <- a.indices
      j <- 0 until n
    } {
      d(i)(j) = a(i)(j)
    }

    val t2 = System.currentTimeMillis()
    println(s"for : ${t2 - t1}")
  }


  populateArray()
  val t = System.currentTimeMillis()
  val k = 500
  for (k <- 0 until k) {
//    forLoop()   // avg 59 // avg 54
//    arrayCopy() // avg 54 // avg 54
//    mapClone()  // avg 49 // avg 49
  }

  val t2 = System.currentTimeMillis()

  println(s"AVG: ${(t2 - t) / k.toDouble}ms")










}
