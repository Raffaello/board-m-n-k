package game

import game.types.BoardMNSize

// TODO: Remove Board2d Array later on...
class BoardTicTacToe extends BoardMNK(BoardMNSize(3, 3), 3) with Board2dArray
