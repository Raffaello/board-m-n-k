package benchmarks

import ai._
import ai.old.{TranspositionTable, GetBoard}
import game.{BoardTicTacToe2, Score}

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
    val board = new BoardTicTacToe2
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = ai.old.minimax(board, isMaximizingPlayer = true)
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
    val board = new BoardTicTacToe2 with MiniMax
    val start = System.currentTimeMillis()
    val score = board.solve
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
    val board = new BoardTicTacToe2 with MiniMaxRaw
    val start = System.currentTimeMillis()
    val score: Score = board.solve
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
    val board = new BoardTicTacToe2
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    val score = ai.old.negamax(board, 1)
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
    val board = new BoardTicTacToe2 with NegaMax
    val start = System.currentTimeMillis()
    val score = board.solve
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
    val board = new BoardTicTacToe2
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
    val board = new BoardTicTacToe2 with AlphaBeta
    val start = System.currentTimeMillis()
    val score = board.solve
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
    println("\nAlpha Beta With TranspositionTableOld:")
    val board = new BoardTicTacToe2 with TranspositionTable with GetBoard
    val start = System.currentTimeMillis()
    Stats.totalCalls = 0
    Stats.cacheHits = 0
    val transposition = ai.old.alphaBetaWithMem(board, board)
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

  {
    println("\nAlpha Beta With TranspositionTable:")
    val board = new BoardTicTacToe2 with ai.TranspositionTable
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

  {
    println("\nAlpha Beta With TranspositionTable Trait:")
    val board = new BoardTicTacToe2 with AlphaBetaTransposition
    val start = System.currentTimeMillis()
    val score = board.solve
    val end = System.currentTimeMillis()
    println(s"total time: ${end - start}")
    println(s"Total calls: ${board.Stats.totalCalls}")
    println(s"Total cache: ${board.transpositions.size}")
    println(s"Total cache Hit: ${board.Stats.cacheHits}")
    println({
      s"score value = "
    } + {
      gameScore(score)
    })
  }
}
