package game

trait DepthAware {
  protected var _depth: Int = 0

  def depth: Int = _depth
}
