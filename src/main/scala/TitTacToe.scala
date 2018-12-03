import game.BoardTicTacToe
import ai._

import scala.util.Random

object TitTacToe extends App {
  val humanPlayer: Byte = 1
  val computerPlayer: Byte = 2
  val joshuaPlayer: Byte = 1 // :)

  do {
    println(
      """
        | Instructions to decide where to put the piece, press the relative number:
        | 0 | 1 | 2
        | 3 | 4 | 5
        | 6 | 7 | 8
      """.stripMargin)

    println("Number of players (-1 exit)?")

    val numPlayers = scala.io.StdIn.readInt()
    if (numPlayers == -1) System.exit(0)
    val game = new BoardTicTacToe()
    if (numPlayers > 0) {
      println("Do you want to start? [y, yes]")
      val playerStart = scala.io.StdIn.readBoolean()
      var playerTurn = playerStart
      while (!game.ended()) {
        game.display()
        if (playerTurn) {
          var valid = false
          while (!valid) {
            println("Make your move: ")
            val move = scala.io.StdIn.readByte()
            val (i, j) = (move / 3, move % 3)
            valid = game.playMove(i.toShort, j.toShort, humanPlayer)
          }
        }
        else {
          val (score, i, j) = negamaxNextMove(game, -1)
          game.playMove(i, j, computerPlayer)
        }
        playerTurn = !playerTurn
      }

      game.display()
      var winner = ""
      game.score() match {
        case 0 => println("STALEMATE!")
        case 1 => println("Player 1 (Human) wins")
        case -1 => println("Player 2 (Computer) wins")
        case _ => ???
      }
    } else {
      var depth = 0
      // Joshua player
      var joshuaPlay = true //Random.nextBoolean()
      while (!game.ended()) {
        game.display()
        var color: Byte = 0
        var player: Byte = 0
        var a = Double.MinValue
        var b = Double.MaxValue
        if (joshuaPlay)  {
          color = 1
          player = joshuaPlayer
        }
        else {
          color = -1
          player = computerPlayer
        }

//        val (score, i, j) = negamaxNextMove(game, color)
        val (score, i, j, a2 , b2) = alphaBetaNextMove(game, depth, a, b,  joshuaPlay)
        a = a2
        b = b2
        depth +=1

        game.playMove(i, j, player)
        joshuaPlay = !joshuaPlay
      }

      game.display()
      game.score() match {
        case 0 =>
          println("GREETINGS PROFESSOR FALKEN!")
          println(
            """
              |A STRANGE GAME.
              |THE ONLY WINNING MOVE IS
              |NOT TO PLAY.
              |
              |HOW ABOUT A NICE GAME OF CHESS?
            """.stripMargin)

          System.exit(0)
        case 1 => println("[BUG] Player 1 (Joshua) wins ")
        case 2 => println("[BUG] Player 2 (Computer) wins")
      }
    }
  } while (true)
}
