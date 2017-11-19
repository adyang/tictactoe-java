package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private Board board;
	private Player playerTwo;
	private Player playerOne;
	private Game game;

	@Before
	public void setUp() throws Exception {
		board = new Board();
		playerOne = new Player('X', board);
		playerTwo = new Player('O', board);
		game = new Game(board, playerOne, playerTwo);
	}

	@Test
	public void viewBoardStatus() throws Exception {
		char[] statusCells = game.getBoardStatus();
		for (char cell : statusCells)
			assertEquals(0, cell);
	}

	@Test
	public void alternateTurns() throws Exception {
		assertSame(playerOne, game.getCurrentPlayer());
		game.makeMove(0);
		assertSame(playerTwo, game.getCurrentPlayer());
		game.makeMove(1);
		assertSame(playerOne, game.getCurrentPlayer());
	}

	@Test
	public void makeMoves() throws Exception {
		game.makeMove(0);
		game.makeMove(1);
		game.makeMove(2);
		game.makeMove(6);
		game.makeMove(8);
		char[] statusCells = game.getBoardStatus();
		assertEquals('X', statusCells[0]);
		assertEquals('O', statusCells[1]);
		assertEquals('X', statusCells[2]);
		assertEquals('O', statusCells[6]);
		assertEquals('X', statusCells[8]);
	}

	@Test
	public void gameHasWinner() throws Exception {
		game.makeMove(0);
		game.makeMove(3);
		game.makeMove(1);
		game.makeMove(4);
		game.makeMove(2);
		assertTrue(game.hasEnded());
		assertTrue(game.hasWinner());
		assertEquals(playerOne, game.getWinner());
	}

	@Test
	public void gameIsDraw() throws Exception {
		game.makeMove(0);
		game.makeMove(2);
		game.makeMove(1);
		game.makeMove(3);
		game.makeMove(4);
		game.makeMove(7);
		game.makeMove(5);
		game.makeMove(8);
		game.makeMove(6);
		assertTrue(game.hasEnded());
		assertFalse(game.hasWinner());
	}
}
