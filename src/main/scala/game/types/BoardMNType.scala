package game.types

// TODO convert to ad hoc polymorphism? Type Classes?
trait BoardMNType {
  val m: Short

  val n: Short

  protected def board: AnyRef
}
