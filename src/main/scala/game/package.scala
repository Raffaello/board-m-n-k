package object game {
  type Board = Array[Array[Byte]]
  type Position = (Short, Short)
  type AlphaBeta = (Int, Int)
//  type Status[Numeric] = (Numeric, Position)
  type Status = (Int, Position)
  type ABStatus = (AlphaBeta, Status)
}
