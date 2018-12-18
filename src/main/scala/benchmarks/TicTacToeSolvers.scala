package benchmarks

import ai.Stats
import game.{BoardTicTacToe, TranspositionTable}

object TicTacToeSolvers extends App {

  def gameScore(score:Double): String = {
    score match {
      case 0.0 => "STALE GAME"
      case x if x>0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  }

  {
    println("\nMiniMax: ")
    val board = new BoardTicTacToe
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = ai.minimax(board, true)
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${ai.Stats.totalCalls}")
    println({
      s"score value = $score => "
    } + {
      gameScore(score)
    })
  }

  {
    println("\nNegaMax: ")
    val board = new BoardTicTacToe
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = ai.negamax(board, 1)
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${ai.Stats.totalCalls}")
    println({
      s"score value = $score => "
    } + {
      gameScore(score)
    })
  }

  {
    println("\nAlpha Beta")
    val board = new BoardTicTacToe
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = ai.alphaBeta(board)
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${ai.Stats.totalCalls}")
    println({
      s"score value = $score => "
    } + {
      gameScore(score)
    })
  }

  {
    println("\nAlpha Beta With Transposition:")
    val board = new BoardTicTacToe with TranspositionTable
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val transposition = ai.alphaBetaWithMem(board, board)
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${ai.Stats.totalCalls}")
    println(s"Total cache: ${board.transpositions.size}")
    println({
      s"score value = $transposition => "
    } + {
      gameScore(transposition.score)
    })
  }
}
