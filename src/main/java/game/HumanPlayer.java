package game;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
	private static final int UI_OFFSET = 1;

	private char marker;
	private Board board;
	private Scanner scanner;
	private PrintStream printStream;

	public HumanPlayer(char marker, Board board, InputStream inputStream, PrintStream printStream) {
		this.marker = marker;
		this.board = board;
		this.scanner = new Scanner(inputStream);
		this.printStream = printStream;
	}

	@Override
	public char getMarker() {
		return marker;
	}

	@Override
	public void makeMove() {
		int selectedMove = selectMove();
		markBoard(positionOf(selectedMove));
	}

	private int selectMove() {
		Integer selectedMove = null;
		while (selectedMove == null) {
			printMoveRequest();
			selectedMove = trySelectMove();
		}
		return selectedMove;
	}

	private void printMoveRequest() {
		printStream.print("Select a number to make a move: ");
	}

	private Integer trySelectMove() {
		try {
			return requestUserMove();
		} catch (NumberFormatException e) {
			printStream.println("Invalid input. " + hintToSelectValidMove());
		} catch (IllegalArgumentException e) {
			printStream.println("Invalid move selected. " + hintToSelectValidMove());
		}
		return null;
	}

	private Integer requestUserMove() {
		Integer selectedMove = Integer.parseInt(scanner.nextLine());
		if (!board.isValid(positionOf(selectedMove))) {
			throw new IllegalArgumentException();
		}
		return selectedMove;
	}

	private Integer positionOf(Integer selectedMove) {
		return selectedMove - UI_OFFSET;
	}

	private void markBoard(int position) {
		board.mark(position, marker);
	}

	private String hintToSelectValidMove() {
		return "Please select an available number from the board.";
	}
}
