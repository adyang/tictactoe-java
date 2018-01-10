package console;

import game.Board;
import game.Game;
import game.Player;
import boards.ThreeByThreeBoard;
import players.ComputerPlayer;
import players.DefaultPlayerFactory;
import players.HumanPlayer;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static console.ConsoleGame.ANSI_CLEAR_SCREEN;

public class TicTacToeApplication {
    public static void main(String[] args) {
        PlayerFactory playerFactory = new DefaultPlayerFactory(System.in, System.out);
        new TicTacToeApplication(System.in, System.out, playerFactory).run();
    }

    private static final List<Integer> VALID_PLAYER_TYPE_INPUTS = Arrays.asList(1, 2);
    private final Scanner scanner;
    private final PrintStream printStream;
    private final PlayerFactory playerFactory;
    private enum PlayerNumber {
        ONE ("one", 'X', 'O'),
        TWO ("two", 'O', 'X');

        final String text;
        final char marker;
        final char opponentMarker;

        PlayerNumber(String text, char marker, char opponentMarker) {
            this.text = text;
            this.marker = marker;
            this.opponentMarker = opponentMarker;
        }
    }

    public TicTacToeApplication(InputStream inputStream, PrintStream printStream, PlayerFactory playerFactory) {
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
        this.playerFactory = playerFactory;
    }

    public void run() {
        displayWelcome();
        do {
            Game game = configureNewGame();
            game.start();
        } while (playAgain());
    }

    private void displayWelcome() {
        clearScreen();
        printStream.println("===== Welcome to Tic-Tac-Toe =====");
    }

    private void clearScreen() {
        printStream.print(ANSI_CLEAR_SCREEN);
        printStream.flush();
    }

    private Game configureNewGame() {
        Board board = configureBoard();
        Player playerOne = configurePlayer(PlayerNumber.ONE, board);
        Player playerTwo = configurePlayer(PlayerNumber.TWO, board);
        return createGame(board, playerOne, playerTwo);
    }

    private static ThreeByThreeBoard configureBoard() {
        return new ThreeByThreeBoard();
    }

    private Player configurePlayer(PlayerNumber playerNumber, Board board) {
        String selectedType = selectPlayerTypeFor(playerNumber);
        return playerFactory.createPlayer(selectedType, playerNumber.marker, board, playerNumber.opponentMarker);
    }

    private String selectPlayerTypeFor(PlayerNumber playerNumber) {
        Integer selectedTypeNum = null;
        while (selectedTypeNum == null){
            displayPlayerTypeRequestFor(playerNumber);
            selectedTypeNum = trySelectPlayerType();
        }
        return toTypeString(selectedTypeNum);
    }

    private void displayPlayerTypeRequestFor(PlayerNumber playerNumber) {
        String requestTemplate = "Select player %s (%c) type (1 - Human, 2 - Computer): ";
        printStream.print(String.format(requestTemplate, playerNumber.text, playerNumber.marker));
    }

    private Integer trySelectPlayerType() {
        try {
            return requestPlayerType();
        } catch (NumberFormatException e) {
            printStream.println("Invalid input. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            printStream.println("Invalid input. Please select a valid player type.");
        }
        return null;
    }

    private int requestPlayerType() {
        int input = Integer.parseInt(scanner.nextLine());
        if (!VALID_PLAYER_TYPE_INPUTS.contains(input))
            throw new IllegalArgumentException();
        return input;
    }

    private String toTypeString(int selectedType) {
        if (selectedType == 1)
            return "human";
        else if (selectedType == 2)
            return "computer";
        else
            throw new IllegalArgumentException("Invalid selectedType: " + selectedType);
    }

    private Game createGame(Board board, Player playerOne, Player playerTwo) {
        return new ConsoleGame(printStream, board, playerOne, playerTwo);
    }

    private boolean playAgain() {
        displayPlayAgainRequest();
        return requestPlayAgain();
    }

    private void displayPlayAgainRequest() {
        printStream.print("Would you like to play again (y/n)? ");
    }

    private boolean requestPlayAgain() {
        String answer = scanner.nextLine().trim();
        return answer.equalsIgnoreCase("y");
    }
}
