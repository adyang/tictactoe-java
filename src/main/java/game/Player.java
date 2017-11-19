package game;

public class Player {
	private char marker;
	private Board board;

	public Player(char marker) {
		this.marker = marker;
	}

	public void markBoard(int position) {
		board.mark(position, marker);
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
