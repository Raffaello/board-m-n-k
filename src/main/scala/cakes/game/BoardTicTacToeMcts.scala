package cakes.game

import cakes.ai.mcts.MctsBoard
import cakes.game.boards.implementations.Board2dArray

// TODO should not have Board2dArray
// Todo neither lookups....
class BoardTicTacToeMcts extends BoardTicTacToeLookUps with Board2dArray with MctsBoard
