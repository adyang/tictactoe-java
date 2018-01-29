package gui;

import application.PlayerNumber;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gui.DisplayBoard.DisplayCell;

public class JavaFxView implements View {
    private final Scene scene;
    private final GameScene gameScene;
    private final WelcomeScene welcomeScene;

    public JavaFxView(Scene scene) {
        this.scene = scene;
        this.gameScene = new GameScene();
        this.welcomeScene = new WelcomeScene();
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
        executeOnUiThread(() -> {
            scene.setRoot(welcomeScene);
        });
    }

    @Override
    public void displayGameConfig(DisplayGameConfig displayGameConfig) {
        executeOnUiThread(() -> {
            displayMainGameConfig(displayGameConfig);
            displayPlayButton(displayGameConfig.playHandler);
        });
    }

    private void displayMainGameConfig(DisplayGameConfig displayGameConfig) {
        List<GameConfigSection> gameConfigSections = createGameConfigSections(displayGameConfig);
        VBox gameConfigContainer = createGameConfigContainer(gameConfigSections);
        welcomeScene.setGameConfig(gameConfigContainer);
    }

    private List<GameConfigSection> createGameConfigSections(DisplayGameConfig displayGameConfig) {
        GameConfigSection playerOneConfig =
                new GameConfigSection("player-one", "Player One", displayGameConfig.playerTypes);
        GameConfigSection playerTwoConfig =
                new GameConfigSection("player-two", "Player Two", displayGameConfig.playerTypes);
        return Arrays.asList(playerOneConfig, playerTwoConfig);
    }

    private VBox createGameConfigContainer(List<GameConfigSection> gameConfigSections) {
        VBox gameConfigContainer = new VBox();
        gameConfigContainer.getChildren().addAll(gameConfigSections);
        return gameConfigContainer;
    }

    private void displayPlayButton(Runnable playHandler) {
        Button playButton = new Button("Play");
        playButton.setId("play");
        playButton.setOnAction(event -> playHandler.run());
        welcomeScene.setPlayButton(playButton);
    }

    @Override
    public boolean validateGameConfig() {
        return false;
    }

    @Override
    public String getPlayerTypeFor(PlayerNumber playerNumber) {
        return null;
    }

    @Override
    public void displayPlayAgain(Runnable playAgainHandler) {

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

    private static class WelcomeScene extends BorderPane {
        public WelcomeScene() {
            setId("welcome-scene");
            Label welcomeMessage = createWelcomeMessage();
            setTop(welcomeMessage);
        }

        private Label createWelcomeMessage() {
            Label welcomeMessage = new Label("Tic-Tac-Toe");
            welcomeMessage.setId("welcome-message");
            setAlignment(welcomeMessage, Pos.CENTER);
            return welcomeMessage;
        }

        void setGameConfig(Node node) {
            setCenter(node);
        }

        void setPlayButton(Node node) {
            setBottom(node);
        }
    }

    private static class GameConfigSection extends VBox {
        private final ToggleGroup optionGroup;

        public GameConfigSection(String id, String configDisplayName, List<String> configOptions) {
            setId(id);
            Label configName = createConfigName(configDisplayName);
            optionGroup = new ToggleGroup();
            List<RadioButton> optionButtons = createOptionButtons(configOptions, optionGroup);
            HBox optionsContainer = createOptionsContainerWith(optionButtons);
            getChildren().addAll(configName, optionsContainer);
        }

        private Label createConfigName(String configDisplayName) {
            Label configName = new Label(configDisplayName);
            configName.getStyleClass().add("config-name");
            return configName;
        }

        private List<RadioButton> createOptionButtons(List<String> configOptions, ToggleGroup optionGroup) {
            List<RadioButton> optionButtons = new ArrayList<>();
            for (String playerType : configOptions) {
                RadioButton rb = new RadioButton(playerType);
                rb.getStyleClass().add("config-option");
                rb.setToggleGroup(optionGroup);
                optionButtons.add(rb);
            }
            return optionButtons;
        }

        private HBox createOptionsContainerWith(List<RadioButton> optionButtons) {
            HBox optionsContainer = new HBox();
            optionsContainer.getChildren().addAll(optionButtons);
            return optionsContainer;
        }
    }
}
