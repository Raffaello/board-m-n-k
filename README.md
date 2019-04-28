# m-n-k board

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/54629ae88ae34ead96cf6c20e31cfac7)](https://app.codacy.com/app/Raffaello/board-m-n-k?utm_source=github.com&utm_medium=referral&utm_content=Raffaello/board-m-n-k&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/Raffaello/board-m-n-k.svg?branch=master)](https://travis-ci.org/Raffaello/board-m-n-k)
[![codecov](https://codecov.io/gh/Raffaello/board-m-n-k/branch/master/graph/badge.svg)](https://codecov.io/gh/Raffaello/board-m-n-k)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/d2223a115b26431faa31d9da12587d2b)](https://www.codacy.com/app/Raffaello/board-m-n-k?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Raffaello/board-m-n-k&amp;utm_campaign=Badge_Coverage)

Special case is (3,3,3) that is tic-tac-toe

## Tic-tac-toe

It includes an easter egg.

## Generalized M,N,K game

- [x] 2 players
- [ ] p players (MNKP)

## Connect 4

- [ ] define the basic rules and write tests 

## Connect 5

- [ ] generalize 4-in-a-row to 5

## Connect K

- [ ] proportionally defining the board size
- [ ] accepting also N,M for the board size 
- [ ] generalize in connect N

## Minimax

- [x] done

## Negamax

- [x] done

## Alpha-Beta

- [x] done

### Basic score function

return just 1, 0, -1.

### depth optimization score function

combined with a less depth solution to gain a higher score, it reduces the search space even more when pruning.

`score + (Signum(score) * (1.0 / (depth + 1.0) ))`

Probably doesn't improve. [Double check from AlphaBeta and above for improvements, benchmark]

## Transposition Table

basic hash function flattening the board of string value statuses.

### improved hashing function

- [ ] ??? 

### Zobrist Hashing

- [ ] implement
- [ ] bitmap?

## improved game dynamics

As a the board get bigger, performances start to be an issue.
Here some techniques designed/reviewed to speed-up the game-dynamics

### endgame

- [X] can be ended only when at least 2k-1 moves are done,
so avoid to check if depth is lower than 2k-1 and just return false or do not check at all.

- [X] keep a counter of empty cells, each time and modifying accordingly when do/undo a move
  check directly if the counter is equal zero to determine end game.

### Board status look up

- [X] considering delta changes in the board,
keeping the previous check board value (no winners)
and check only around the move done instead of all the board.

### moves generations

- [ ] detect quickly if next move would be a winner, 
  so put it as first one in moves generation
  Possible with counters for rows,cols and diags when reach K-1 value,
  the first move is the one that make the score in the generations.
  
- [ ] So check if player can win in this turn and than do that move only. all others can be pruned directly.
- [ ] if the next turn the opponent can win, generate only that move and all other can pruned directly.  

- [ ] Order for moves could be by max symbols for row, cols, diags in case of tie, otherwise sort descending.
  So it should end up quickly and start pruning more.
  It is important to be very fast in the ordering and pruning enough.

### Board representation

from 2D array to ??? (1D? easier for hashing?)

- [ ] replace with 1D array and compute the moves as `i*n+j` for the exact index position

- [ ] simplify the checking of end game looking around the value and moving trough the array.
- [ ] simplify hashing.
- [ ] consider to use a bit board for each player to represent the game.

## Principal Variation

## MTD-f

## Best_Node_Search

## Monte Carlo Tree Search

- [ ] basic implementation
- [ ] multi threading
- [ ] advanced???

## AlphaZero

## Results

## references

- [MIT Search: Games, Minimax and Alpha-Beta](https://www.youtube.com/watch?v=STjW3eH0Cik)
- [Minimax](https://en.wikipedia.org/wiki/Minimax)
- [Negamax](https://en.wikipedia.org/wiki/Negamax)
- [Alpha–beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)
- [Zobrist Hashing](https://en.wikipedia.org/wiki/Zobrist_hashing)
- [MCTS Part I](https://www.cs.swarthmore.edu/~bryce/cs63/s16/slides/2-15_MCTS.pdf)
- [MCTS Part II](https://www.cs.swarthmore.edu/~bryce/cs63/s16/slides/2-17_extending_mcts.pdf)
- [MCTS Part III](http://ccg.doc.gold.ac.uk/ccg_old/teaching/ludic_computing/ludic16.pdf)
- [MCTS Intro](https://jeffbradberry.com/posts/2015/09/intro-to-monte-carlo-tree-search/)
- [MCTS](https://www.baeldung.com/java-monte-carlo-tree-search)
