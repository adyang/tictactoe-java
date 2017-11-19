package game;

public class Game {
	private Board board;
	private Player[] players = new Player[2];
	private int currentPlayerIdx;

	public Game(Board board, Player playerOne, Player playerTwo) {
		this.board = board;
		this.players[0] = playerOne;
		this.players[1] = playerTwo;
	}

	public char[] getBoardStatus() {
		return board.getStatus();
	}

	public Player getCurrentPlayer() {
		return players[currentPlayerIdx];
	}

	public void makeMove(int position) {
		players[currentPlayerIdx].markBoard(position);
		currentPlayerIdx = (currentPlayerIdx + 1) % 2;
	}

	public boolean hasEnded() {
		return board.hasEnded();
	}

	public boolean hasWinner() {
		return board.hasWinner();
	}

	public Player getWinner() {
		for (Player player : players)
			if (player.getMarker() == board.getWinner())
				return player;
		return null;
	}
}
