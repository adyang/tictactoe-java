package application;

import game.Board;
import game.Game;
import game.Player;

public abstract class TicTacToeApplication {
    protected enum PlayerNumber {
        ONE ("one", 'X', 'O'),
        TWO ("two", 'O', 'X');

        public final String text;
        public final char marker;
        public final char opponentMarker;

        PlayerNumber(String text, char marker, char opponentMarker) {
            this.text = text;
            this.marker = marker;
            this.opponentMarker = opponentMarker;
        }
    }

    protected final PlayerFactory playerFactory;

    public TicTacToeApplication(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
    }

    public void run() {
        do {
            displayWelcome();
            Game game = configureNewGame();
            game.start();
        } while (playAgain());
    }

    protected abstract void displayWelcome();

    private Game configureNewGame() {
        Board board = configureBoard();
        Player playerOne = configurePlayer(PlayerNumber.ONE, board);
        Player playerTwo = configurePlayer(PlayerNumber.TWO, board);
        return createGame(board, playerOne, playerTwo);
    }

    protected abstract Board configureBoard();

    protected abstract Player configurePlayer(PlayerNumber playerNumber, Board board);

    protected abstract Game createGame(Board board, Player playerOne, Player playerTwo);

    protected abstract boolean playAgain();
}
