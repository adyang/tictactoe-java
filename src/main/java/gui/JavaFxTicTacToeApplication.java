package gui;

import boards.ThreeByThreeBoard;
import game.Board;
import game.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.HumanPlayer;

import java.util.concurrent.ArrayBlockingQueue;

public class JavaFxTicTacToeApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group(),500,500);
        scene.getStylesheets().add(getClasspathResourceUrl("/css/tictactoe.css"));
        View view = new JavaFxView(scene);

        GuiGame game = configureNewGame(view);
        game.displayBoardStatus();
        game.displayCurrentTurn(new HumanPlayer('X', null, System.in, System.out));
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();
    }

    private String getClasspathResourceUrl(String resourcePath) {
        return getClass().getResource(resourcePath).toExternalForm();
    }

    private GuiGame configureNewGame(View view) {
        Board board = new ThreeByThreeBoard();
        board.mark(0, 'O');
        Player playerOne = new HumanPlayer('O', board, System.in, System.out);
        Player playerTwo = new HumanPlayer('X', board, System.in, System.out);
        return new GuiGame(board, playerOne, playerTwo, view, new ArrayBlockingQueue<>(1));
    }

    private DisplayBoard createBoard(String... cells) {
        DisplayBoard board = new DisplayBoard();
        for (int i = 0; i < cells.length; i++)
            board.cells.add(new DisplayBoard.DisplayCell(cells[i], i, xPos(i), yPos(i)));
        return board;
    }

    private int xPos(int i) {
        return i % 3;
    }

    private int yPos(int i) {
        return i / 3;
    }
}
