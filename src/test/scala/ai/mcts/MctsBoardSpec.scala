package ai.mcts

import game.{Board, BoardMNKPLookUp, Position}
import org.scalatest.{FlatSpec, Matchers}

class MctsBoardSpec extends FlatSpec with Matchers {

  // TODO this imply a non-well design traits/classes, re extend from itself
  // Mcts board already extending from BoardMNKPLookup, here some sort of cycle because of the missing paramter
  // for the constructor: m,n,k,p that cannot be passed in the trait (yet)
  sealed class MctsBoardStub(m: Short, n: Short, k: Short, p: Byte) extends BoardMNKPLookUp(m, n, k, p) with MctsBoard {
    def getBoard(): Board = _board
  }

  def initBoard(m: Short, n: Short, k: Short, p: Byte): MctsBoard = new MctsBoardStub(m, n, k, p)

  "MctsBoard" should "be initied" in {
    val game = new MctsBoardStub(3, 3, 3, 2)
    game shouldBe an[MctsBoard]
  }

  it should "generate all possible moves for tic-tac-toe" in {
    val game = initBoard(3, 3, 3, 2)
    val moves = game.allPossibleMoves()
    moves should have length 9
    moves should equal(IndexedSeq[Position]((0, 0), (0, 1), (0, 2), (1, 0), (1, 1), (1, 2), (2, 0), (2, 1), (2, 2)))
  }

  it should "generate a random move" in {
    val game = initBoard(3, 3, 3, 2)
    val randomMove = game.randomMove()
    randomMove shouldNot be(None)
    val (x, y) = randomMove.get
    x shouldBe (1.toShort +- 1)
    y shouldBe (1.toShort +- 1)
  }

  it should "not generate a random move" in {
    val game = initBoard(3, 3, 3, 2)
    for {
      i <- game.mIndices
      j <- game.nIndices
    } game.playMove((i, j), 1)

    val randomMove = game.randomMove()
    randomMove shouldBe None
  }

  it should "play a random move" in {
    val game = initBoard(3, 3, 3, 2)
    val randomMove = game.playRandomMove(1)
    randomMove shouldBe true
  }

  it should "not play a random move" in {
    val game = initBoard(3, 3, 3, 2)
    for {
      i <- game.mIndices
      j <- game.nIndices
    } game.playMove((i, j), 1)

    val randomMove = game.playRandomMove(2)
    randomMove shouldBe false
  }

  it should "deep cloned" in {
    val game = initBoard(3, 3, 3, 2).asInstanceOf[MctsBoardStub]
    game.playRandomMove(1)
    val gameClone = game.clone().asInstanceOf[MctsBoardStub]

    game ne gameClone shouldBe true
    game.depth() shouldBe gameClone.depth()
    game.getBoard() ne gameClone.getBoard() shouldBe true
    game.getBoard() shouldBe gameClone.getBoard()
    game.allPossibleMoves() shouldBe gameClone.allPossibleMoves()
    game.LookUps() ne gameClone.LookUps() shouldBe true

    game.LookUps().cols ne gameClone.LookUps().cols
    game.LookUps().cols(0) ne gameClone.LookUps().cols(0)
    game.LookUps().cols shouldBe gameClone.LookUps().cols
    game.LookUps().rows ne gameClone.LookUps().rows
    game.LookUps().rows shouldBe gameClone.LookUps().rows

    game.playRandomMove(2)

    game ne gameClone shouldBe true
    game.depth() shouldBe gameClone.depth() + 1
    game.getBoard() ne gameClone.getBoard() shouldBe true
    game.getBoard() should not be gameClone.getBoard()
    game.allPossibleMoves() should not be gameClone.allPossibleMoves()

    game.LookUps() ne gameClone.LookUps()

    game.LookUps().cols ne gameClone.LookUps().cols
    game.LookUps().cols should not be gameClone.LookUps().cols
    game.LookUps().rows ne gameClone.LookUps().rows
    game.LookUps().rows should not be gameClone.LookUps().rows
  }
}
