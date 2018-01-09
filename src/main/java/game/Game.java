package game;

public abstract class Game {
	protected Board board;
	private Player[] players = new Player[2];
	private int currentPlayerIdx;

	public Game(Board board, Player playerOne, Player playerTwo) {
		this.board = board;
		this.players[0] = playerOne;
		this.players[1] = playerTwo;
	}

	public void start() {
		printWelcome();
		while (!board.hasEnded()) {
			printBoardStatus();
			printCurrentTurn(getCurrentPlayer());
			makeCurrentPlayerMove();
		}
		printEndStatus();
	}

	private Player getCurrentPlayer() {
		return players[currentPlayerIdx];
	}

	private void makeCurrentPlayerMove() {
		players[currentPlayerIdx].makeMove();
		currentPlayerIdx = nextPlayerIdx();
	}

	private int nextPlayerIdx() {
		return (currentPlayerIdx + 1) % players.length;
	}

	public abstract void printWelcome();

	public abstract void printBoardStatus();

	public abstract void printCurrentTurn(Player currentPlayer);

	public abstract void printEndStatus();
}
