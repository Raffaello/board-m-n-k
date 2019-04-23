package game

class BoardConnect4 {
  // board M=6 N=7
  // move only for N (column)
  // in the column starting from the bottom add the cell at the first 0
  // the board represented by the array is flipped (top-down instead of bottom-up)

  // apart the move method, it is similar to tic-tac-toe,
  // this move only on a column also reduce the search space for each turn, the size is N instead of MxN
  // basically it is practically the same algorithm and representation but just with the row gravity rule applied.

  // consider first to switch to 1D board representation and refactor the code

  // consider to clean up the code too first

  // the AI algorithm has to be changed for the moves generator that basically is jut 1..N :)

  // Probably would be better implementing MCTS algorithm first...
  // There is no much to do here apart code refactoring and re-architecting a little to accomodate this
  // vertical board.

  // More interesting is the AI part in MCTS..
}
