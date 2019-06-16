package game

import ai.mcts.MctsBoard
import game.boards.implementations.Board2dArray

// TODO should not have Board2dArray
// Todo neither lookups....
class BoardTicTacToeMcts extends BoardTicTacToeLookUps with Board2dArray with MctsBoard
