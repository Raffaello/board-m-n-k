import com.sun.org.apache.xalan.internal.xsltc.dom.BitArray

import scala.collection.mutable

package object game {
  type Board = Board2d
  type Position = (Short, Short)
  type Score = Int
  type Status = (Score, Position)
  type Player = Byte
  type Board2d = Array[Array[Byte]]
  type Board1d = Array[Byte]
  type BitBoard = mutable.BitSet
//  type BitBoardOpt = Int
}

