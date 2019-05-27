package game.boards

trait BoardDepthAware {
  protected var _depth: Int = 0

  def depth: Int = _depth
}
