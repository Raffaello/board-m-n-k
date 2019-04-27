package ai

trait AiStats {
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }
}
