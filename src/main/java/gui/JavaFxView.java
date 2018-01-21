package gui;

import application.PlayerNumber;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static gui.DisplayBoard.DisplayCell;

public class JavaFxView implements View {
    private final Scene scene;
    private final GameScene gameScene;

    public JavaFxView(Scene scene) {
        this.scene = scene;
        this.gameScene = new GameScene();
    }

    @Override
    public void displayBoard(final DisplayBoard displayBoard) {
        executeOnUiThread(() -> {
            GridPane boardGrid = convertToBoardGrid(displayBoard);
            gameScene.setBoard(boardGrid);
            scene.setRoot(gameScene);
        });
    }

    private GridPane convertToBoardGrid(DisplayBoard displayBoard) {
        GridPane boardGrid = createBoardGrid();
        for (DisplayCell displayCell : displayBoard.cells) {
            Label cell = convertToCell(displayCell);
            boardGrid.add(cell, displayCell.xPos, displayCell.yPos);
        }
        return boardGrid;
    }

    private GridPane createBoardGrid() {
        GridPane boardGrid = new GridPane();
        boardGrid.setId("board");
        return boardGrid;
    }

    private Label convertToCell(DisplayCell displayCell) {
        Label cell = createCell(displayCell);
        addHandler(cell, displayCell.actionHandler);
        return cell;
    }

    private Label createCell(DisplayCell displayCell) {
        Label cell = new Label(displayCell.marker);
        cell.setId("cell-" + displayCell.idxPos);
        cell.getStyleClass().add("cell");
        if (displayCell.xPos == 0)
            cell.getStyleClass().add("left-edge-cell");
        if (displayCell.yPos == 0)
            cell.getStyleClass().add("top-edge-cell");
        if (displayCell.xPos == 2)
            cell.getStyleClass().add("right-edge-cell");
        if (displayCell.yPos == 2)
            cell.getStyleClass().add("bottom-edge-cell");
        return cell;
    }

    private void addHandler(Label cell, Runnable handler) {
        if (handler != null)
            cell.setOnMousePressed(event -> {
                removeHandlerFromAllCells();
                handler.run();
            });
    }

    private void removeHandlerFromAllCells() {
        gameScene.lookupAll(".cell").forEach(cell -> cell.setOnMousePressed(null));
    }

    @Override
    public void displayCurrentMarker(String marker) {
        executeOnUiThread(() -> {
            gameScene.setGameMessage(marker + " player's Turn");
        });
    }

    @Override
    public void displayWinner(final String winner) {
        executeOnUiThread(() -> {
            gameScene.setGameMessage(winner + " has Won!");
        });
    }

    @Override
    public void displayDraw() {
        executeOnUiThread(() -> {
            gameScene.setGameMessage("Draw!");
        });
    }

    @Override
    public void displayWelcome() {

    }

    @Override
    public void displayGameConfig(DisplayGameConfig displayGameConfig) {

    }

    @Override
    public boolean validateGameConfig() {
        return false;
    }

    @Override
    public String getPlayerTypeFor(PlayerNumber playerNumber) {
        return null;
    }

    private void executeOnUiThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    private static class GameScene extends BorderPane {
        private final Label gameMessage;

        public GameScene() {
            setId("game-scene");
            gameMessage = new Label();
            gameMessage.setId("game-message");
            setAlignment(gameMessage, Pos.CENTER);
            setTop(gameMessage);
        }

        void setGameMessage(String text) {
            gameMessage.setText(text);
        }

        void setBoard(Node node) {
            setCenter(node);
        }
    }
}
