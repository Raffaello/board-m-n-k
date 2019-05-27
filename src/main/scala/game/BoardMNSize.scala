package game

final case class BoardMNSize(m: Short, n: Short) {
  require(m > 2 && n > 2)
}
