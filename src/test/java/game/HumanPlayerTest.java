package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HumanPlayerTest {
	private Board board;
	private Player player;
	private ByteArrayOutputStream outputStream;
	private ByteArrayInputStream inputStream;

	@Before
	public void setUp() throws Exception {
		board = new ThreeByThreeBoard();
		outputStream = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() throws Exception {
		outputStream.close();
		inputStream.close();
	}

	@Test
	public void printInputRequest() throws Exception {
		makeMoveWithInput(toInputString("5"));
		assertOutputStreamEquals("Select a number to make a move: ");
	}


	@Test
	public void readUserInput_validInput() throws Exception {
		makeMoveWithInput(toInputString("5"));
		assertEquals('X', board.getStatus()[4]);
	}

	@Test
	public void readUserInput_invalidInput() throws Exception {
		makeMoveWithInput(toInputString("W", "5"));
		assertOutputStreamContains("Invalid input. Please select an available number from the board.");
	}

	@Test
	public void readUserInput_inputOutOfRange() throws Exception {
		makeMoveWithInput(toInputString("10", "5"));
		assertOutputStreamContains("Invalid move selected. Please select an available number from the board.");
	}

	@Test
	public void readUserInput_validInputAfterReEnter() throws Exception {
		makeMoveWithInput(toInputString("10", "5"));
		assertEquals('X', board.getStatus()[4]);
	}

	private void assertOutputStreamEquals(String expectedMessage) throws UnsupportedEncodingException {
		assertEquals(expectedMessage, outputStream.toString(StandardCharsets.UTF_8.name()));
	}

	private void assertOutputStreamContains(String expectedMessage) throws UnsupportedEncodingException {
		assertTrue(outputStream.toString(StandardCharsets.UTF_8.name()).contains(expectedMessage));
	}

	private void makeMoveWithInput(String userInput) {
		inputStream = new ByteArrayInputStream(
				String.format(userInput).getBytes(StandardCharsets.UTF_8));
		player = new HumanPlayer('X', board, inputStream, new PrintStream(outputStream));

		player.makeMove();
	}

	private String toInputString(String... inputs) {
		return String.join("%n", inputs) + "%n";
	}
}
