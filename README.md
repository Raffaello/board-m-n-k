# m-n-k board

Special case is (3,3,3) that is tic-tac-toe


## Tic-tac-toe

At the moment is the only one implemented and playable board. Plus a easter egg.


## Minimax


## Negamax


## alpha-beta


### Basic


### depth optimization

combined with a less depth solution to gain a higher score, it reduces the search space even more.


## Transposition Table

### improved hashing function

### improved game checks

#### endgame

can be ended only when at least 2k-1 moves are done,
so avoid check if depth is lower than 2k-1 and just return false.

- keep a counter of empty cells, each time and modifying accordingly when do/undo a move
  check directly if the counter is equal zero to determine end game.

#### Board status look up

considering delta changes in the board,
keeping the previous check board value (no winners)
and check only around the move done instead of all the board.

#### Board representation

from 2D array to ??? (1D? easier for hashing?)


## Principal Variation


## Monte Carlo Tree Search


## Results


## generalizing to (m,n,k) game


## references

- [MIT Search: Games, Minimax and Alpha-Beta](https://www.youtube.com/watch?v=STjW3eH0Cik)
- [Minimax](https://en.wikipedia.org/wiki/Minimax)
- [Negamax](https://en.wikipedia.org/wiki/Negamax)
- [Alpha–beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)