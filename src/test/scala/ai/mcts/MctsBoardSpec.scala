package ai.mcts

import game.{BoardTicTacToeMcts, Score}
import game.Implicit.convertToPlayer
import game.types.{Position, Status}
import org.scalatest.{FlatSpec, Matchers}

class MctsBoardSpec extends FlatSpec with Matchers {

  // This imply a non-well design traits/classes, re extend from itself
  // Mcts board already extending from BoardMNKPLookup, here some sort of cycle because of the missing parameter
  // for the constructor: m,n,k,p that cannot be passed in the trait (yet)
  sealed class MctsBoardStub(m: Short, n: Short, k: Short) extends BoardTicTacToeMcts {
    override def board: Board = _board
  }


  def initBoard(m: Short, n: Short, k: Short): MctsBoard = new MctsBoardStub(m, n, k)

  "MctsBoard" should "be initialized" in {
    val game = initBoard(3, 3, 3)
    game shouldBe an[MctsBoard]
  }

  it should "generate all possible moves for tic-tac-toe" in {
    val game = initBoard(3, 3, 3)
    val moves = game.allPossibleMoves()
    moves should have length 9
    moves should equal(IndexedSeq[Position](
      Position(0, 0),
      Position(0, 1),
      Position(0, 2),
      Position(1, 0),
      Position(1, 1),
      Position(1, 2),
      Position(2, 0),
      Position(2, 1),
      Position(2, 2))
    )
  }

  it should "generate a random move" in {
    val game = initBoard(3, 3, 3)
    val randomMove = game.randomMove()
    randomMove shouldNot be(None)
    val pos = randomMove.get
    pos.row shouldBe (1.toShort +- 1)
    pos.col shouldBe (1.toShort +- 1)
  }

  it should "not generate a random move" in {
    val game = initBoard(3, 3, 3)
    for {
      i <- game.mIndices
      j <- game.nIndices
    } game.playMove(Position(i, j), 1)

    val randomMove = game.randomMove()
    randomMove shouldBe None
  }

  it should "play a random move" in {
    val game = initBoard(3, 3, 3)
    val randomMove = game.playRandomMove(1)
    randomMove shouldBe true
  }

  it should "not play a random move" in {
    val game = initBoard(3, 3, 3)
    for {
      i <- game.mIndices
      j <- game.nIndices
    } game.playMove(Position(i, j), 1)

    val randomMove = game.playRandomMove(2)
    randomMove shouldBe false
  }

  it should "be deep cloned" in {
    val game = initBoard(3, 3, 3).asInstanceOf[MctsBoardStub]
    game.playRandomMove(1)
    val gameClone = game.clone().asInstanceOf[MctsBoardStub]

    game ne gameClone shouldBe true
    game.depth shouldBe gameClone.depth
    game.board ne gameClone.board shouldBe true
    game.board shouldBe gameClone.board
    game.allPossibleMoves() shouldBe gameClone.allPossibleMoves()
    game.lookUps ne gameClone.lookUps shouldBe true

    game.lookUps.cols ne gameClone.lookUps.cols
    game.lookUps.cols(0) ne gameClone.lookUps.cols(0)
    game.lookUps.cols shouldBe gameClone.lookUps.cols
    game.lookUps.rows ne gameClone.lookUps.rows
    game.lookUps.rows shouldBe gameClone.lookUps.rows

    game.playRandomMove(2)

    game ne gameClone shouldBe true
    game.depth shouldBe gameClone.depth + 1
    game.board ne gameClone.board shouldBe true
    game.board should not be gameClone.board
    game.allPossibleMoves() should not be gameClone.allPossibleMoves()

    game.lookUps ne gameClone.lookUps

    game.lookUps.cols ne gameClone.lookUps.cols
    game.lookUps.cols should not be gameClone.lookUps.cols
    game.lookUps.rows ne gameClone.lookUps.rows
    game.lookUps.rows should not be gameClone.lookUps.rows
  }

  it should "solve" in {
    val game = initBoard(3, 3, 3)
    game.solve shouldBe 0
  }

  it should "have next move" in {
    val game = initBoard(3, 3, 3)
    game.nextMove shouldBe Status[Score](0, Position(0, 0))
    game.playMove(Position(0,0), game.nextPlayer())
    game.nextMove shouldBe Status[Score](0, Position(1,1))
  }

  it should "have 2nd next Move" in {
    val game = initBoard(3, 3, 3)
    game.playMove(Position(0,0), game.nextPlayer()) shouldBe true
    game.nextMove shouldBe Status[Score](0, Position(1,1))
  }
}
