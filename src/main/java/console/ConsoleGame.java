package console;

import game.Board;
import game.Game;
import game.Player;

import java.io.PrintStream;

public class ConsoleGame extends Game {
	static final String ANSI_CLEAR_SCREEN = "\033[H\033[2J";
	private PrintStream outputStream;

	public ConsoleGame(PrintStream outputStream, Board board, Player playerOne, Player playerTwo) {
		super(board, playerOne, playerTwo);
		this.outputStream = outputStream;
	}

	@Override
	public void printBoardStatus() {
		clearScreen();
		printBoard();
	}

	private void clearScreen() {
		outputStream.print(ANSI_CLEAR_SCREEN);
		outputStream.flush();
	}

	private void printBoard() {
		String boardString = "";
		char[] cells = board.getStatus();
		for (int i = 0; i < cells.length; i++) {
			boardString += cellAt(i, cells);
			if (!isLastCellInRow(i))
				boardString += " | ";
			if (isLastCellButNotLastRow(cells.length, i))
				boardString += String.format("%n---------%n");
		}
		outputStream.println(boardString);
	}

	private String cellAt(int i, char[] cells) {
		if (cellIsEmpty(i, cells))
			return String.valueOf(i + 1);
		else
			return String.valueOf(cells[i]);
	}

	private boolean cellIsEmpty(int i, char[] cells) {
		return cells[i] == 0;
	}

	private boolean isLastCellButNotLastRow(int numCells, int i) {
		return isLastCellInRow(i) && isNotLastCell(numCells, i);
	}

	private boolean isLastCellInRow(int i) {
		return i % board.size() == board.size() - 1;
	}

	private boolean isNotLastCell(int numCells, int i) {
		return i != numCells - 1;
	}

	@Override
	public void printEndStatus() {
		clearScreen();
		printBoard();
		printGameOutcome();
	}

	private void printGameOutcome() {
		if (board.hasWinner())
			outputStream.println(board.getWinner() + " has Won!");
		else
			outputStream.println("The game is a Draw!");
	}

	@Override
	public void printWelcome() {
		clearScreen();
		outputStream.println("===== Welcome to Tic-Tac-Toe =====");
	}

	@Override
	public void printCurrentTurn(Player currentPlayer) {
		outputStream.println(currentPlayer.getMarker() + " Turn");
	}
}
