package game;

public class Player {
	private char marker;
	private Board board;

	public Player(char marker, Board board) {
		this.marker = marker;
		this.board = board;
	}

	public void markBoard(int position) {
		board.mark(position, marker);
	}

	public char getMarker() {
		return marker;
	}
}
