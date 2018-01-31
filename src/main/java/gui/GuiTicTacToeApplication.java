package gui;

import application.PlayerFactory;
import application.PlayerNumber;
import application.TicTacToeApplication;
import boards.ThreeByThreeBoard;
import game.Board;
import game.Game;
import game.Player;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class GuiTicTacToeApplication extends TicTacToeApplication {
    private static final String HUMAN = "Human";
    private static final String COMPUTER = "Computer";
    private final BlockingQueue<String> playerTypeQueue;
    private final BlockingQueue<Boolean> playAgainQueue;
    private final BlockingQueue<Integer> moveQueue;
    private final View view;

    public GuiTicTacToeApplication(BlockingQueue<String> playerTypeQueue, BlockingQueue<Boolean> playAgainQueue,
                                   BlockingQueue<Integer> moveQueue, View view, PlayerFactory playerFactory) {
        super(playerFactory);
        this.playerTypeQueue = playerTypeQueue;
        this.playAgainQueue = playAgainQueue;
        this.moveQueue = moveQueue;
        this.view = view;
    }

    @Override
    protected void displayWelcome() {
        view.displayWelcome();
        DisplayGameConfig gameConfig = createDisplayGameConfig();
        view.displayGameConfig(gameConfig);
    }

    private DisplayGameConfig createDisplayGameConfig() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = Arrays.asList(HUMAN, COMPUTER);
        gameConfig.playHandler = playHandler();
        return gameConfig;
    }

    private Runnable playHandler() {
        return () -> {
            boolean isValidConfig = view.validateGameConfig();
            if (isValidConfig)
                addSelectedPlayerTypesToQueue();
        };
    }

    private void addSelectedPlayerTypesToQueue() {
        for (PlayerNumber playerNumber : PlayerNumber.values()) {
            String displayPlayerType = view.getPlayerTypeFor(playerNumber);
            String playerType = toFactoryPlayerType(displayPlayerType);
            playerTypeQueue.add(playerType);
        }
    }

    private String toFactoryPlayerType(String displayPlayerType) {
        if (HUMAN.equals(displayPlayerType))
            return "guiHuman";
        else if (COMPUTER.equals(displayPlayerType))
            return "computer";
        else
            throw new IllegalArgumentException("Invalid displayPlayerType: " + displayPlayerType);
    }

    @Override
    protected Board configureBoard() {
        return new ThreeByThreeBoard();
    }

    @Override
    protected Player configurePlayer(PlayerNumber playerNumber, Board board) {
        try {
            String selectedPlayerType = playerTypeQueue.take();
            return playerFactory.createPlayer(selectedPlayerType, playerNumber.marker, board, playerNumber.opponentMarker);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for player type selection.", e);
        }
    }

    @Override
    protected Game createGame(Board board, Player playerOne, Player playerTwo) {
        return new GuiGame(board, playerOne, playerTwo, view, moveQueue);
    }

    @Override
    protected boolean playAgain() {
        view.displayPlayAgain(playAgainHandler());
        return tryWaitForPlayAgainResponse();
    }

    private Runnable playAgainHandler() {
        return () -> playAgainQueue.add(true);
    }

    private boolean tryWaitForPlayAgainResponse() {
        try {
            return playAgainQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for play again selection.", e);
        }
    }
}
