package gui;

import application.PlayerFactory;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.DefaultPlayerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaFxMain extends Application {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = setupScene();
        GuiTicTacToeApplication application = setUpGuiTicTacToeApplication(scene);
        executor.execute(application::run);
        setupAndShow(stage, scene);
    }

    private Scene setupScene() {
        Scene scene = new Scene(new Group(),500,500);
        scene.getStylesheets().add(getClasspathResourceUrl("/css/tictactoe.css"));
        return scene;
    }

    private String getClasspathResourceUrl(String resourcePath) {
        return getClass().getResource(resourcePath).toExternalForm();
    }

    private GuiTicTacToeApplication setUpGuiTicTacToeApplication(Scene scene) {
        View view = new JavaFxView(scene);
        ArrayBlockingQueue<String> playerTypeQueue = new ArrayBlockingQueue<>(2);
        ArrayBlockingQueue<Boolean> playAgainQueue = new ArrayBlockingQueue<>(1);
        ArrayBlockingQueue<Integer> moveQueue = new ArrayBlockingQueue<>(1);
        PlayerFactory playerFactory = new DefaultPlayerFactory(System.in, System.out, moveQueue);
        return new GuiTicTacToeApplication(playerTypeQueue, playAgainQueue, moveQueue, view, playerFactory);
    }

    private void setupAndShow(Stage stage, Scene scene) {
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("JavaFx Application Thread Interrupted.");
        } finally {
            executor.shutdownNow();
        }
    }
}
