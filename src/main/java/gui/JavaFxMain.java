package gui;

import boards.ThreeByThreeBoard;
import game.Board;
import game.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.GuiHumanPlayer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaFxMain extends Application {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group(),500,500);
        scene.getStylesheets().add(getClasspathResourceUrl("/css/tictactoe.css"));
        View view = new JavaFxView(scene);

        GuiGame game = configureNewGame(view);
        executor.execute(game::start);

        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();
    }

    private String getClasspathResourceUrl(String resourcePath) {
        return getClass().getResource(resourcePath).toExternalForm();
    }

    private GuiGame configureNewGame(View view) {
        Board board = new ThreeByThreeBoard();
        ArrayBlockingQueue<Integer> moveQueue = new ArrayBlockingQueue<>(1);
        Player playerOne = new GuiHumanPlayer('X', board, moveQueue);
        Player playerTwo = new GuiHumanPlayer('O', board, moveQueue);
        return new GuiGame(board, playerOne, playerTwo, view, moveQueue);
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

    @Override
    public void stop() throws Exception {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Game Thread Interrupted.");
        } finally {
            executor.shutdownNow();
        }
    }
}
