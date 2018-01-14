package console;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import game.Board;
import game.Player;
import boards.ThreeByThreeBoard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import game.TestPlayer;

public class ConsoleGameTest {
	private static final String NEW_LINE = System.lineSeparator();
	private static final String EMPTY_BOARD = 	"1 | 2 | 3" + NEW_LINE +
												"---------" + NEW_LINE +
												"4 | 5 | 6" + NEW_LINE +
												"---------" + NEW_LINE +
												"7 | 8 | 9" + NEW_LINE;
	private static final String DRAW_BOARD = 	"X | O | X" + NEW_LINE +
												"---------" + NEW_LINE +
												"O | X | O" + NEW_LINE +
												"---------" + NEW_LINE +
												"O | X | O" + NEW_LINE +
												"The game is a Draw!" + NEW_LINE;
	private ConsoleGame game;
	private ByteArrayOutputStream outputStream;
	private Board board;

	@Before
	public void setUp() throws Exception {
		board = new ThreeByThreeBoard();
		outputStream = new ByteArrayOutputStream();
		game = new ConsoleGame(new PrintStream(outputStream), board, null, null);
	}

	@After
	public void tearDown() throws Exception {
		outputStream.close();
	}

	@Test
	public void displayCurrentPlayerTurn() throws Exception {
		Player player = new TestPlayer('X', board);

		game.displayCurrentTurn(player);

		assertOutputStream("X Turn" + NEW_LINE);
	}

	@Test
	public void displayBoardStatus_emptyBoard() throws Exception {
		game.displayBoardStatus();

		assertOutputStreamWithClearScreenPrepend(EMPTY_BOARD);
	}

	@Test
	public void displayBoardStatus_filledBoard() throws Exception {
		board.mark(0, 'X');
		board.mark(1, 'O');
		board.mark(2, 'X');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'O');
		board.mark(6, 'O');
		board.mark(7, 'X');

		game.displayBoardStatus();
		
		String expectedString = "X | O | X" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | X | O" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | X | 9" + NEW_LINE;
		assertOutputStreamWithClearScreenPrepend(expectedString);
	}

	@Test
	public void displayEndStatus_crossWins() throws Exception {
		board.mark(0, 'X');
		board.mark(1, 'O');
		board.mark(2, 'X');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'O');
		board.mark(6, 'O');
		board.mark(7, 'X');
		board.mark(8, 'X');
		
		game.displayEndStatus();
		
		String expectedString = "X | O | X" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | X | O" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | X | X" + NEW_LINE +
								"X has Won!" + NEW_LINE;
		assertOutputStreamWithClearScreenPrepend(expectedString);
	}

	@Test
	public void displayEndStatus_circleWins() throws Exception {
		board.mark(1, 'X');
		board.mark(0, 'O');
		board.mark(2, 'X');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'O');
		board.mark(8, 'X');
		board.mark(6, 'O');
		
		game.displayEndStatus();
		
		String expectedString = "O | X | X" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | X | O" + NEW_LINE +
								"---------" + NEW_LINE +
								"O | 8 | X" + NEW_LINE +
								"O has Won!" + NEW_LINE;
		assertOutputStreamWithClearScreenPrepend(expectedString);
	}

	@Test
	public void displayEndStatus_drawGame() throws Exception {
		board.mark(0, 'X');
		board.mark(1, 'O');
		board.mark(2, 'X');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'O');
		board.mark(6, 'O');
		board.mark(7, 'X');
		board.mark(8, 'O');
		
		game.displayEndStatus();
		
		assertOutputStreamWithClearScreenPrepend(DRAW_BOARD);
	}
	

	@Test
	public void displayBoardStatus_clearOutputForEachCall() throws Exception {
		game.displayBoardStatus();
		game.displayBoardStatus();

		assertOutputStream(ConsoleGame.ANSI_CLEAR_SCREEN + EMPTY_BOARD + ConsoleGame.ANSI_CLEAR_SCREEN + EMPTY_BOARD);
	}

	@Test
	public void displayEndStatus_clearOutputForEachCall() throws Exception {
		game.displayBoardStatus();
		board.mark(0, 'X');
		board.mark(1, 'O');
		board.mark(2, 'X');
		board.mark(3, 'O');
		board.mark(4, 'X');
		board.mark(5, 'O');
		board.mark(6, 'O');
		board.mark(7, 'X');
		board.mark(8, 'O');
		game.displayEndStatus();
		
		assertOutputStream(ConsoleGame.ANSI_CLEAR_SCREEN + EMPTY_BOARD + ConsoleGame.ANSI_CLEAR_SCREEN + DRAW_BOARD);
	}

	private void assertOutputStreamWithClearScreenPrepend(String expectedString) throws UnsupportedEncodingException {
		assertOutputStream(prependClearScreen(expectedString));
	}

	private void assertOutputStream(String expectedString) throws UnsupportedEncodingException {
		assertEquals(expectedString, outputStream.toString(StandardCharsets.UTF_8.name()));
	}

	private String prependClearScreen(String str) {
		return ConsoleGame.ANSI_CLEAR_SCREEN + str;
	}
}
