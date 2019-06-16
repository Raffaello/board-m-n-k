import cakes.ai.{TranspositionTable1dArrayString, _}
import cakes.game.BoardMNKP
import cakes.game.Implicit.convertToPlayer
import cakes.game.boards.implementations.{Board1dArray, Board2dArray}
import cakes.game.types.{BOARD_1D_ARRAY, BOARD_2D_ARRAY, BoardMNTypeEnum}
import org.scalatest.{Matchers, WordSpec}

class BoardMNK3Spec extends WordSpec with Matchers {

  private[this] object Board3p {
    def apply(boardType: BoardMNTypeEnum, algo: String): AiBoard = {
      algo match {
        case "miniMax" => miniMax(boardType)
        case "negaMax" => negaMax(boardType)
        case "alphaBeta" => alphaBeta(boardType)
        case "alphaBetaTT" => alphaBetaTT(boardType)
      }
    }

    def miniMax(boardType: BoardMNTypeEnum): AiBoard = {
      boardType match {
        case BOARD_2D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board2dArray with MiniMax
        case BOARD_1D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board1dArray with MiniMax
      }
    }

    def negaMax(boardType: BoardMNTypeEnum): AiBoard = {
      boardType match {
        case BOARD_2D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board2dArray with NegaMax
        case BOARD_1D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board1dArray with NegaMax
      }
    }

    def alphaBeta(boardType: BoardMNTypeEnum): AiBoard = {
      boardType match {
        case BOARD_2D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board2dArray with AlphaBeta
        case BOARD_1D_ARRAY => new BoardMNKP(3, 3, 3, 3) with Board1dArray with AlphaBeta
      }
    }

    def alphaBetaTT(boardType: BoardMNTypeEnum): AiBoard = {
      boardType match {
        case BOARD_2D_ARRAY => new BoardMNKP(3, 3, 3, 3)
          with Board2dArray with AlphaBetaTransposition with TranspositionTable2dArrayString
        case BOARD_1D_ARRAY => new BoardMNKP(3, 3, 3, 3)
          with Board1dArray with AlphaBetaTransposition with TranspositionTable1dArrayString
      }
    }
  }

  "BoardMNKP(3,3,3,3)" should {
    for (b <- Seq(BOARD_2D_ARRAY, BOARD_1D_ARRAY)) {
      s"$b" should {
        "draw with" should {
          for (a <- Seq("miniMax", "negaMax", "alphaBeta", "alphaBetaTT")) {
            s"$a" in {
              val game = Board3p.miniMax(b)
              game.nextPlayer() shouldBe 1
              game.solve shouldBe 0
            }
          }
        }
      }
    }
  }
}
