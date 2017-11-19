package game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private Board board;
	private Player playerCross;
	private Player playerCircle;

	@Before
	public void setUp() throws Exception {
		board = new Board();
		playerCross = new Player('X', board);
		playerCircle = new Player('O', board);
	}

	@Test
	public void markBoard() {
		playerCross.markBoard(0);
		char[] statusCells = board.getStatus();
		assertEquals('X', statusCells[0]);
	}

	@Test
	public void markBoardMultiPlayer() {
		playerCross.markBoard(0);
		playerCircle.markBoard(1);
		char[] statusCells = board.getStatus();
		assertEquals('X', statusCells[0]);
		assertEquals('O', statusCells[1]);
	}
}
