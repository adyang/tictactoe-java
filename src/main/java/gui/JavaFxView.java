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

import java.util.*;

import static gui.DisplayBoard.DisplayCell;

public class JavaFxView implements View {
    public static final String PLAYER_ONE_DISPLAY_NAME = "Player One";
    public static final String PLAYER_TWO_DISPLAY_NAME = "Player Two";
    private final Scene scene;
    private final GameScene gameScene;
    private final WelcomeScene welcomeScene;
    private Map<PlayerNumber, GameConfigSection> gameConfigSectionMap;

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
        gameConfigSectionMap = createGameConfigSectionMap(displayGameConfig);
        VBox gameConfigContainer = createGameConfigContainer(gameConfigSectionMap.values());
        welcomeScene.setGameConfig(gameConfigContainer);
    }

    private Map<PlayerNumber, GameConfigSection> createGameConfigSectionMap(DisplayGameConfig displayGameConfig) {
        SortedMap<PlayerNumber, GameConfigSection> map = new TreeMap<>();
        map.put(PlayerNumber.ONE,
                new GameConfigSection("player-one", PLAYER_ONE_DISPLAY_NAME, displayGameConfig.playerTypes));
        map.put(PlayerNumber.TWO,
                new GameConfigSection("player-two", PLAYER_TWO_DISPLAY_NAME, displayGameConfig.playerTypes));
        return map;
    }

    private VBox createGameConfigContainer(Collection<GameConfigSection> gameConfigSections) {
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
        clearErrorMessages();
        boolean isValidPlayerOne = validatePlayerConfig(gameConfigSectionMap.get(PlayerNumber.ONE), PLAYER_ONE_DISPLAY_NAME);
        boolean isValidPlayerTwo = validatePlayerConfig(gameConfigSectionMap.get(PlayerNumber.TWO), PLAYER_TWO_DISPLAY_NAME);
        return isValidPlayerOne && isValidPlayerTwo;
    }

    private void clearErrorMessages() {
        for (GameConfigSection gameConfigSection : gameConfigSectionMap.values()) {
            gameConfigSection.clearErrorMessage();
        }
    }

    private boolean validatePlayerConfig(GameConfigSection playerConfig, String playerDisplayName) {
        if (playerConfig.hasNoSelection()) {
            playerConfig.displayErrorMessage(String.format("Please select player type for %s.", playerDisplayName));
            return false;
        }
        return true;
    }

    @Override
    public String getPlayerTypeFor(PlayerNumber playerNumber) {
        return gameConfigSectionMap.get(playerNumber).getSelection();
    }

    @Override
    public void displayPlayAgain(Runnable playAgainHandler) {
        executeOnUiThread(() -> {
            Button playAgainButton = new Button("Play Again?");
            playAgainButton.setId("play-again");
            playAgainButton.setOnAction(event -> playAgainHandler.run());
            gameScene.setPlayAgainButton(playAgainButton);
        });
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

        void setPlayAgainButton(Node node) {
            setBottom(node);
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
        private final Label errorMessage;

        public GameConfigSection(String id, String configDisplayName, List<String> configOptions) {
            setId(id);
            Label configName = createConfigName(configDisplayName);
            optionGroup = new ToggleGroup();
            List<RadioButton> optionButtons = createOptionButtons(configOptions, optionGroup);
            HBox optionsContainer = createOptionsContainerWith(optionButtons);
            errorMessage = createErrorMessage();
            getChildren().addAll(configName, optionsContainer, errorMessage);
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

        private Label createErrorMessage() {
            Label errorMessage = new Label();
            errorMessage.getStyleClass().add("error-message");
            errorMessage.setVisible(false);
            return errorMessage;
        }

        public boolean hasNoSelection() {
            return optionGroup.getSelectedToggle() == null;
        }

        public void displayErrorMessage(String message) {
            errorMessage.setText(message);
            errorMessage.setVisible(true);
        }

        public void clearErrorMessage() {
            errorMessage.setVisible(false);
        }

        public String getSelection() {
            RadioButton selectedButton = (RadioButton) optionGroup.getSelectedToggle();
            return selectedButton.getText();
        }
    }
}
