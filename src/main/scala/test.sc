val k = 3
val p = 1
val n = 5
val j = 1
Array.fill(k)(p)

Array.fill(n)(0)
  .updated(0, Array.fill(k)(p))

Array.tabulate(n)(i => if(i>=j && i< k+j)p else 0)
