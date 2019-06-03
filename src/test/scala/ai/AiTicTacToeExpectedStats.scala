package ai

import org.scalatest.Matchers

trait AiTicTacToeExpectedStats extends Matchers {
  // TODO this force to run the suite not in parallel
  ai.Stats.totalCalls = 0
  ai.Stats.cacheHits = 0

  final val minimaxTotalCallsDepth0 = 294778

  final val alphaBetaTotalCallsDepth0 = 12413

  final val alphaBetaWithMemTotalCallsDepth0 = 4520
  final val alphaBetaWithMemTotalCacheHitsDepth0 = 10690
  final val withMemSize = 5478

  final val alphaBetaTTTTotalCalls = 4187
  final val alphaBetaTTTTotalCacheSize = 1308
  final val alphaBetaTTTTotalCaheHit = 3851

  def expMiniMax(totalCalls: Int): Unit = {
    totalCalls shouldBe minimaxTotalCallsDepth0
  }

  def expMiniMax(): Unit = expMiniMax(ai.Stats.totalCalls)

  def expNegamax(totalCalls: Int): Unit = expMiniMax(totalCalls)

  def expNegamax(): Unit = expMiniMax(ai.Stats.totalCalls)

  def expAlphaBeta(totalCalls: Int): Unit = {
    totalCalls shouldBe alphaBetaTotalCallsDepth0
  }

  def expAlphaBeta(): Unit = expAlphaBeta(ai.Stats.totalCalls)

  def expAlphaBetaWithMemStats(totalCalls: Int, totalCache: Int, memSize: Int): Unit = {
    totalCalls shouldBe alphaBetaWithMemTotalCallsDepth0
    totalCache shouldBe alphaBetaWithMemTotalCacheHitsDepth0
    memSize shouldBe withMemSize
  }

  def expAlphaBetaWithMemStats(memSize: Int): Unit = {
    expAlphaBetaWithMemStats(ai.Stats.totalCalls, ai.Stats.cacheHits, memSize)
  }

  def expAlphaBetaTTTrait(totalCalls: Int, totalCache: Int, memSize: Int): Unit = {
    totalCalls shouldBe alphaBetaTTTTotalCalls
    totalCache shouldBe alphaBetaTTTTotalCaheHit
    memSize shouldBe alphaBetaTTTTotalCacheSize
  }
}