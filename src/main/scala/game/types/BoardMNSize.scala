package game.types

final case class BoardMNSize(m: Short, n: Short) {
  require(m > 2 && n > 2)
}
