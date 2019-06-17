package cakes.ai

trait AiStats {
  // TODO refactor to internally increment the values.
  // TODO totalCalls "always", cacheHits only with "transposition table"
  object Stats {
    var totalCalls: Int = 0
    var cacheHits: Int = 0
  }
}
