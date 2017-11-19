package game;

import java.util.Scanner;

public class TicTacToeApplication {
	public static void main(String[] args) {
		Game game = setupGame();
		System.out.println("===== Welcome to Tic-Tac-Toe =====");
		try (Scanner sc = new Scanner(System.in)) {
			while (!game.hasEnded()) {
				printBoardStatus(game);
				System.out.println(game.getCurrentPlayer().getMarker() + " Turn");
				System.out.println("Select a number to make a move: ");

				try {
					int playerMove = Integer.parseInt(sc.nextLine());
					game.makeMove(playerMove);
				} catch (Exception e) {
					System.out.println("Please select a valid position.");
				}
				System.out.println();
			}
		}
		printBoardStatus(game);

		if (game.hasWinner())
			System.out.println(game.getWinner().getMarker() + " has Won!");
		else
			System.out.println("The game is a Draw!");
	}

	private static void printBoardStatus(Game game) {
		String boardStatusStr = "";
		String[][] cells = getFormattedBoardStatus(game);
		for (int i = 0; i < 3; i++) {
			boardStatusStr += String.format("%s | %s | %s\n", cells[i][0], cells[i][1], cells[i][2]);
			if (i < 2)
				boardStatusStr += "---------\n";
		}
		System.out.println(boardStatusStr);
	}

	private static String[][] getFormattedBoardStatus(Game game) {
		String[][] displayCells = new String[3][3];
		char[] boardCells = game.getBoardStatus();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				char cell = boardCells[i * 3 + j];
				if (cell == 0)
					displayCells[i][j] = String.valueOf(i * 3 + j);
				else
					displayCells[i][j] = String.valueOf(cell);
			}
		}
		return displayCells;
	}

	private static Game setupGame() {
		Board board = new Board();
		Player playerOne = new Player('X', board);
		Player playerTwo = new Player('O', board);
		return new Game(board, playerOne, playerTwo);
	}
}
