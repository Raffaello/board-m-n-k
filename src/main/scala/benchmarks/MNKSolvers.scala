package benchmarks

import ai._
import ai.old.BoardMNKwithGetBoard
import game.BoardMNK

import scala.collection.immutable.NumericRange

object MNKSolvers extends App {
  def gameScore(score: Double): String = {
    score match {
      case 0.0 => "STALE GAME"
      case x if x > 0.0 => "P1 WIN"
      case _ => "P2 WIN"
    }
  }

  for {
    k <- NumericRange[Short](3, 4, 1)
    n <- NumericRange[Short](3, 4, 1)
    m <- NumericRange[Short](3, 4, 1)
    if k <= n || k <= m
  } {
    println(s"m = $m --- n = $n --- k = $k")

    {
      println("\nMiniMax: ")
      val board = new BoardMNK(m, n, k)
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
      val board = new BoardMNK(m, n, k) with MiniMax
      val start = System.currentTimeMillis()
      //    Stats.totalCalls = 0
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
      val board = new BoardMNK(m, n, k) with MiniMaxRaw
      val start = System.currentTimeMillis()
      //    Stats.totalCalls = 0
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
      println("\nNegaMax: ")
      val board = new BoardMNK(m, n, k)
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
      val board = new BoardMNK(m, n, k) with NegaMax
      val start = System.currentTimeMillis()
      //    Stats.totalCalls = 0
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
      val board = new BoardMNK(m, n, k)
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
      val board = new BoardMNK(m, n, k) with AlphaBeta
      val start = System.currentTimeMillis()
      //    Stats.totalCalls = 0
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
      println("\nAlpha Beta With TranspositionTableOld:")
      val board = new BoardMNKwithGetBoard(m, n, k) with ai.old.TranspositionTable
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
      val board = new BoardMNK(m, n, k) with ai.TranspositionTable
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
      val board = new BoardMNK(m, n, k) with AlphaBetaTransposition
      val start = System.currentTimeMillis()
      Stats.totalCalls = 0
      Stats.cacheHits = 0
      val transposition = board.solve()
      val end = System.currentTimeMillis()
      println(s"total time: ${end - start}")
      println(s"Total calls: ${board.Stats.totalCalls}")
      println(s"Total cache: ${board.transpositions.size}")
      println(s"Total cache Hit: ${board.Stats.cacheHits}")
      println({
        s"score value = $transposition => "
      } + {
        gameScore(transposition.score)
      })
    }
  }
}
