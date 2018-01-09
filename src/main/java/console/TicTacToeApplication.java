package console;

import game.Board;
import game.Player;
import boards.ThreeByThreeBoard;
import players.ComputerPlayer;
import players.HumanPlayer;

public class TicTacToeApplication {
	public static void main(String[] args) {
		Board board = new ThreeByThreeBoard();
		Player playerOne = new HumanPlayer('X', board, System.in, System.out);
		Player playerTwo = new ComputerPlayer('O', board, playerOne.getMarker());
		new ConsoleGame(System.out, board, playerOne, playerTwo).start();
	}
}
