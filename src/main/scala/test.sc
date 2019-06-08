val b = Array.ofDim[Int](2)

b(0) = 4
b(1) = 4

b(0).toHexString
b(1).toHexString
b.foldLeft(0)((acc, b) => acc | b)