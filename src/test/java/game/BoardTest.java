package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
	private Board board;
	private char[] statusCells;

	@Before
	public void setUp() throws Exception {
		board = new Board();
	}

	@Test
	public void newBoard() {
		statusCells = board.getStatus();
		for (char cell : statusCells) {
			assertEquals(0, cell);
		}
		assertFalse(board.hasEnded());
	}

	@Test
	public void markBoard() throws Exception {
		board.mark(0, 'X');
		board.mark(1, 'O');
		board.mark(5, 'X');
		board.mark(8, 'O');
		statusCells = board.getStatus();
		assertEquals('X', statusCells[0]);
		assertEquals('O', statusCells[1]);
		assertEquals('X', statusCells[5]);
		assertEquals('O', statusCells[8]);
	}

	@Test
	public void boardHasWinner_firstHorizontal() throws Exception {
		markBoardAtPositions('X', 0, 1, 2);
		markBoardAtPositions('O', 4, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_secondHorizontal() throws Exception {
		markBoardAtPositions('X', 0, 1);
		markBoardAtPositions('O', 3, 4, 5);
		assertWinnerIs('O');
	}

	@Test
	public void boardHasWinner_thirdHorizontal() throws Exception {
		markBoardAtPositions('X', 6, 7, 8);
		markBoardAtPositions('O', 4, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_firstVertical() throws Exception {
		markBoardAtPositions('X', 0, 3, 6);
		markBoardAtPositions('O', 4, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_secondVertical() throws Exception {
		markBoardAtPositions('X', 1, 4, 7);
		markBoardAtPositions('O', 3, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_thirdVertical() throws Exception {
		markBoardAtPositions('X', 2, 5, 8);
		markBoardAtPositions('O', 3, 6);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_firstDiagonal() throws Exception {
		markBoardAtPositions('X', 0, 4, 8);
		markBoardAtPositions('O', 3, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardHasWinner_secondDiagonal() throws Exception {
		markBoardAtPositions('X', 2, 4, 6);
		markBoardAtPositions('O', 3, 5);
		assertWinnerIs('X');
	}

	@Test
	public void boardIsDrawNoWinner() throws Exception {
		board.mark(0, 'X');
		board.mark(1, 'X');
		board.mark(2, 'O');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'X');
		board.mark(6, 'X');
		board.mark(7, 'O');
		board.mark(8, 'O');
		assertTrue(board.hasEnded());
		assertFalse(board.hasWinner());
	}

	@Test
	public void getWinner_WithoutCheckingHasWinner() throws Exception {
		markBoardAtPositions('X', 2, 4, 6);
		markBoardAtPositions('O', 3, 5);
		assertEquals('X', board.getWinner());
	}

	@Test
	public void exceptionOnMarkingSamePosition() throws Exception {
		try {
			board.mark(0, 'X');
			board.mark(0, 'O');
			fail("Should throw exception on marking same position.");
		} catch (IllegalArgumentException e) {
			assertEquals("Position 0 is already marked.", e.getMessage());
		}
	}

	private void markBoardAtPositions(char marker, int... positions) {
		for (int position : positions)
			board.mark(position, marker);
	}

	private void assertWinnerIs(char winner) {
		assertTrue(board.hasEnded());
		assertTrue(board.hasWinner());
		assertEquals(winner, board.getWinner());
	}
}