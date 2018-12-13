package object game {
  type Board = Array[Array[Byte]]

  sealed abstract class FlagEnum
  case object FlagEnumUpperBound extends FlagEnum
  case object FlagEnumLowerBound extends FlagEnum
  case object FlagEnumScore extends FlagEnum
}
