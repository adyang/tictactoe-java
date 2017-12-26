package game;

public class TicTacToeApplication {
	public static void main(String[] args) {
		Board board = new ThreeByThreeBoard();
		Player playerOne = new HumanPlayer('X', board, System.in, System.out);
		Player playerTwo = new HumanPlayer('O', board, System.in, System.out);
		new ConsoleGame(System.out, board, playerOne, playerTwo).start();
	}
}
