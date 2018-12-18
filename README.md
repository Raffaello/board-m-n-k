# m-n-k board

Special case is (3,3,3) that is tic-tac-toe


## Tic-tac-toe

It includes an easter egg.

## Generalized M,N,K game

## Minimax

## Negamax

## Alpha-Beta

### Basic score function

return just 1, 0, -1.

### depth optimization score function

combined with a less depth solution to gain a higher score, it reduces the search space even more when pruning.

`score + (Signum(score) * (1.0 / (depth + 1.0) ))`

## Transposition Table

basic hash function flattening the board ot string value statuses.

### improved hashing function

### Zobrist Hashing

## improved game dynamics

As a the board get bigger, performances start to be an issue.
Here some techniques designed/reviewed to speed-up the game-dynamics

### endgame

- [X] can be ended only when at least 2k-1 moves are done,
so avoid to check if depth is lower than 2k-1 and just return false or do not check at all.

- [X] keep a counter of empty cells, each time and modifying accordingly when do/undo a move
  check directly if the counter is equal zero to determine end game.

### Board status look up

- [ ] considering delta changes in the board,
keeping the previous check board value (no winners)
and check only around the move done instead of all the board.

### moves generations

- [ ] detect quickly if next move would be a winner, 
  so put it as first one in moves generation
  Possible with counters for rows,cols and diags when reach K-1 value,
  the first move is the one that make the score in the generations.
  
- [ ] So check if player can win in this turn and than do that move only. all others can be pruned directly.
- [ ] if the next turn the opponent can win, generate only that move and all other can pruned directly.  

- [ ] Order for moves could be by max symbols for row,cols,diag.
  So it should end up quickly and start pruning more.
  It is important to be very fast in the ordering and pruning enough.

### Board representation

from 2D array to ??? (1D? easier for hashing?)

## Principal Variation

## Monte Carlo Tree Search

## Results

## references

- [MIT Search: Games, Minimax and Alpha-Beta](https://www.youtube.com/watch?v=STjW3eH0Cik)
- [Minimax](https://en.wikipedia.org/wiki/Minimax)
- [Negamax](https://en.wikipedia.org/wiki/Negamax)
- [Alpha–beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)
- [Zobrist Hashing](https://en.wikipedia.org/wiki/Zobrist_hashing)
