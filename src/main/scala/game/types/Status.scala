package game.types

final case class Status[T: Numeric](score: T, position: Position)
