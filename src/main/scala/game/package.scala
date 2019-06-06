import scala.language.implicitConversions

package object game {
  object Implicit {
    implicit def convertToPlayer(x: Int): Player = x.toByte
  }

  type Score = Int
  type Player = Byte

  type Board1d = Array[Player]
  type Board2d = Array[Array[Player]]
  type BitBoard = Int
  type BitBoardPlayers = Array[BitBoard]
}

