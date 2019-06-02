package game.types

// TODO not really useful, can just be removed and use plain m and n
final case class BoardMNSize(m: Short, n: Short) {
  require(m > 2 && n > 2)
}
