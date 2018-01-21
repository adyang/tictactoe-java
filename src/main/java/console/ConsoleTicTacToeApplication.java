package console;

import application.PlayerFactory;
import application.TicTacToeApplication;
import boards.ThreeByThreeBoard;
import game.Board;
import game.Game;
import game.Player;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static console.ConsoleGame.ANSI_CLEAR_SCREEN;

public class ConsoleTicTacToeApplication extends TicTacToeApplication {
    private static final List<Integer> VALID_PLAYER_TYPE_INPUTS = Arrays.asList(1, 2);
    private final Scanner scanner;
    private final PrintStream printStream;

    public ConsoleTicTacToeApplication(InputStream inputStream, PrintStream printStream, PlayerFactory playerFactory) {
        super(playerFactory);
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    @Override
    protected void displayWelcome() {
        clearScreen();
        printStream.println("===== Welcome to Tic-Tac-Toe =====");
    }

    private void clearScreen() {
        printStream.print(ANSI_CLEAR_SCREEN);
        printStream.flush();
    }

    @Override
    protected Board configureBoard() {
        return new ThreeByThreeBoard();
    }

    @Override
    protected Player configurePlayer(PlayerNumber playerNumber, Board board) {
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

    @Override
    protected Game createGame(Board board, Player playerOne, Player playerTwo) {
        return new ConsoleGame(printStream, board, playerOne, playerTwo);
    }

    @Override
    protected boolean playAgain() {
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
