import scala.util.hashing.MurmurHash3
//val k = 3
//val p = 1
//val n = 5
//val j = 1
//Array.fill(k)(p)
//
//Array.fill(n)(0)
//  .updated(0, Array.fill(k)(p))

//Array.tabulate(n)(i => if(i>=j && i< k+j)p else 0)


Math.signum(-21)

val a = Array.ofDim[Byte](3,3)


a.map(_.mkString("")).mkString("")

val h1 = MurmurHash3.orderedHash(a)
a(0)(0)=1
a.map(_.mkString("")).mkString("")
val h2 =MurmurHash3.orderedHash(a)

h1 == h2