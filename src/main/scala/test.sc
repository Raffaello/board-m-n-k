import ai.mcts.{MctsBoard, State}
import ai.old.withGetBoard
import game._

val g = new BoardTicTacToe() with MctsBoard with withGetBoard
val player: Byte = 2
val state = State(g, player)

val states = state.allPossibleStates()

states.toList
val h = states.head
val perm = states.permutations.foreach(println)

println (states == states.head)
println()
for (i<- states.indices) {
  val s1 = states(i)
  for (j <- states.indices) {
    val s2 = states(j)
    if (i < j) {
      println(s1 == s2)

      if (s1 == s2) {
        println (s"s1 board: ${s1.board} --- ${s2.board}")
      }
    }
  }
}

states.foreach(println)

//import scala.util.Random
//import scala.util.hashing.MurmurHash3
//val k = 3
//val p = 1
//val n = 3
//val j = 1
//val m = 3
//
//
////Array.fill(k)(p)
//val aaa = Array.ofDim[Int](3,3, 2)
//val aaaa = Array.fill[Int](3,3,2)(Random.nextInt())
//aaaa.flatten.flatten.length
//
//val z = Array.ofDim[Int](3,3)
//val pos = (0, 0)
//
//z(pos)
//
//Array.fill(n)(0)
//  .updated(0, Array.fill(k)(p))
//
//Array.tabulate(n)(i => if(i>=j && i< k+j)p else 0)
////Array.tabulate(m,n,2)
////m
//Math.signum(-21)
//
//val a = Array.ofDim[Byte](3,3)
//
//
//a.map(_.mkString("")).mkString("")
//
//val h1 = MurmurHash3.orderedHash(a)
//a(0)(0)=1
//a.map(_.mkString("")).mkString("")
//val h2 =MurmurHash3.orderedHash(a)
//
//h1 == h2