package benchmarks

import ai._
import game.BoardTicTacToe

object TicTacToeSolvers extends App {

  def gameScore(score: Double): String = {
    score match {
      case 0.0 => "STALE GAME"
      case x if x > 0.0 => "P1 WIN"
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
    println("\nMiniMax trait: ")
    val board = new BoardTicTacToe with MiniMax
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = board.solve()
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${board.Stats.totalCalls}")
    println({
      s"score value = $score => "
    } + {
      gameScore(score)
    })
  }
  {
    println("\nMiniMax trait RAW: ")
    val board = new BoardTicTacToe with MiniMax
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = board.solveRaw()
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${board.Stats.totalCalls}")
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
    println("\nNegaMax trait: ")
    val board = new BoardTicTacToe with NegaMax
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = board.solve()
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${board.Stats.totalCalls}")
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
    println("\nAlphaBeta trait: ")
    val board = new BoardTicTacToe with AlphaBeta
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = board.solve()
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${board.Stats.totalCalls}")
    println({
      s"score value = $score => "
    } + {
      gameScore(score)
    })
  }
  {
    println("\nAlpha Beta With Transposition:")
    val board = new BoardTicTacToe with TranspositionTableOld
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val transposition = ai.alphaBetaWithMem(board, board)
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${ai.Stats.totalCalls}")
    println(s"Total cache: ${board.transpositions.size}")
    println(s"Total cache Hit: ${ai.Stats.cacheHits}")
    println({
      s"score value = $transposition => "
    } + {
      gameScore(transposition.score)
    })
  }
}
