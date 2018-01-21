package application;

import game.Board;
import game.Game;
import game.Player;

public abstract class TicTacToeApplication {

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
