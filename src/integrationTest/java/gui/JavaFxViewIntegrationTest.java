package gui;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static gui.DisplayBoard.DisplayCell;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class JavaFxViewIntegrationTest extends ApplicationTest {
    private static final int BOARD_SIZE = 3;
    public static final List<String> DISPLAY_PLAYER_TYPES = Arrays.asList("Human", "Computer");
    private View view;
    private Scene scene;
    private Group dummyGroup;

    @Override
    public void start(Stage stage) {
        dummyGroup = new Group();
        scene = new Scene(dummyGroup, 500, 500);
        stage.setScene(scene);
        stage.show();
        view = new JavaFxView(scene);
    }

    @Test
    public void displayBoard_shouldHaveCorrectNumberOfCells() {
        DisplayBoard board = createBoard(
                " ", " ", " ",
                " ", " ", " ",
                " ", " ", " ");

        view.displayBoard(board);

        waitForFxEvents();
        GridPane boardGrid = lookup("#board").query();
        assertEquals(9, boardGrid.getChildren().size());
    }

    @Test
    public void displayBoard_shouldHaveCorrectMarkerInCell() {
        DisplayBoard board = createBoard(
                " ", " ", " ",
                " ", " ", "X",
                "O", " ", " ");

        view.displayBoard(board);

        waitForFxEvents();
        verifyThat("#cell-5", hasText("X"));
        assertGridPosition("#cell-5", 2, 1);
        verifyThat("#cell-6", hasText("O"));
        assertGridPosition("#cell-6", 0, 2);
    }

    @Test
    public void displayBoard_shouldTriggerHandlerOnClick() {
        List<Integer> triggeredCells = new ArrayList<>();
        DisplayBoard board = createBoardWithTriggerHandlerCells(triggeredCells);

        displayAndClickOnAllCells(board);

        waitForFxEvents();
        assertEquals(listOfIntegersInRange(0, 9), triggeredCells);
    }

    @Test
    public void displayBoard_whenDisplayCellHasNoHandler_shouldNotTriggerExceptionOnClick() {
        DisplayBoard board = new DisplayBoard();
        board.cells.add(new DisplayCell("X", 0, 0, 0));

        view.displayBoard(board);

        waitForFxEvents();
        clickOn("#cell-0");
    }

    @Test
    public void displayBoard_whenCellClicked_shouldNotTriggerFurtherHandler() {
        List<Integer> triggeredCells = new ArrayList<>();
        DisplayBoard board = createBoardWithTriggerHandlerCells(triggeredCells);

        view.displayBoard(board);
        waitForFxEvents();
        clickOn("#cell-0");
        clickOnAllCells();

        assertEquals(Arrays.asList(0), triggeredCells);
    }

    @Test
    public void displayBoard_shouldSwitchSceneRootToGame() {
        DisplayBoard board = new DisplayBoard();

        view.displayBoard(board);

        waitForFxEvents();
        assertNotSame(dummyGroup, scene.getRoot());
        assertTrue(lookup("#game-scene").tryQuery().isPresent());
    }

    @Test
    public void displayCurrentMarker_shouldDisplayCurrentMarkerMessage() {
        DisplayBoard board = new DisplayBoard();

        view.displayBoard(board);
        view.displayCurrentMarker("X");

        waitForFxEvents();
        verifyThat("#game-message", hasText("X player's Turn"));
    }

    @Test
    public void displayWinner_shouldDisplayWinnerMessage() {
        DisplayBoard board = new DisplayBoard();

        view.displayBoard(board);
        view.displayWinner("X");

        waitForFxEvents();
        verifyThat("#game-message", hasText("X has Won!"));
    }

    @Test
    public void displayDraw_shouldDisplayDrawMessage() {
        DisplayBoard board = new DisplayBoard();

        view.displayBoard(board);
        view.displayDraw();

        waitForFxEvents();
        verifyThat("#game-message", hasText("Draw!"));
    }

    @Test
    public void displayWelcome_shouldSwitchSceneRootToWelcome() {
        view.displayWelcome();

        waitForFxEvents();
        assertNotSame(dummyGroup, scene.getRoot());
        assertTrue(lookup("#welcome-scene").tryQuery().isPresent());
    }

    @Test
    public void displayWelcome_shouldDisplayWelcomeMessage() {
        view.displayWelcome();

        waitForFxEvents();
        verifyThat("#welcome-message", hasText("Tic-Tac-Toe"));
    }

    @Test
    public void displayGameConfig_shouldDisplayPlayerOneConfig() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);

        waitForFxEvents();
        verifyThat("#player-one .config-name", hasText("Player One"));
        assertOptionsHasText("#player-one .config-option", DISPLAY_PLAYER_TYPES);
    }

    @Test
    public void displayGameConfig_shouldDisplayPlayerTwoConfig() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);

        waitForFxEvents();
        verifyThat("#player-two .config-name", hasText("Player Two"));
        assertOptionsHasText("#player-two .config-option", DISPLAY_PLAYER_TYPES);
    }

    @Test
    public void displayGameConfig_shouldDisplayPlayButton() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);

        waitForFxEvents();
        verifyThat("#play", hasText("Play"));
    }

    @Test
    public void displayGameConfig_clickPlayButton_shouldRunPlayHandler() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;
        AtomicBoolean hasHandlerRan = new AtomicBoolean();
        gameConfig.playHandler = () -> hasHandlerRan.set(true);

        view.displayWelcome();
        view.displayGameConfig(gameConfig);

        waitForFxEvents();
        clickOn("#play");
        assertTrue(hasHandlerRan.get());
    }

    @Test
    public void displayPlayAgain_shouldDisplayPlayAgainButton() {
        DisplayBoard board = new DisplayBoard();

        view.displayBoard(board);
        view.displayPlayAgain(null);

        waitForFxEvents();
        verifyThat("#play-again", hasText("Play Again?"));
    }

    @Test
    public void displayPlayAgain_clickPlayAgainButton_shouldRunPlayHandler() {
        DisplayBoard board = new DisplayBoard();
        AtomicBoolean hasHandlerRan = new AtomicBoolean();

        view.displayBoard(board);
        view.displayPlayAgain(() -> hasHandlerRan.set(true));

        waitForFxEvents();
        clickOn("#play-again");
        assertTrue(hasHandlerRan.get());
    }

    @Test
    public void validateGameConfig_whenPlayerTypeNotSelected_shouldReturnFalse() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;
        AtomicBoolean isValidGameConfig = new AtomicBoolean(true);

        view.displayWelcome();
        view.displayGameConfig(gameConfig);
        Platform.runLater(() -> isValidGameConfig.set(view.validateGameConfig()));

        waitForFxEvents();
        assertFalse(isValidGameConfig.get());
    }

    @Test
    public void validateGameConfig_whenPlayerOneTypeNotSelected_shouldDisplayErrorMessage() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);
        Platform.runLater(() -> view.validateGameConfig());

        waitForFxEvents();
        verifyThat("#player-one .error-message", hasText("Please select player type for Player One."));
    }

    @Test
    public void validateGameConfig_whenPlayerTwoTypeNotSelected_shouldDisplayErrorMessage() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);
        Platform.runLater(() -> view.validateGameConfig());

        waitForFxEvents();
        verifyThat("#player-two .error-message", hasText("Please select player type for Player Two."));
    }

    @Test
    public void validateGameConfig_whenAllConfigSelected_shouldReturnTrue() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;
        AtomicBoolean isValidGameConfig = new AtomicBoolean();

        view.displayWelcome();
        view.displayGameConfig(gameConfig);
        waitForFxEvents();
        Node playerOneOption = lookup("#player-one .config-option").match(optionWithText("Human")).query();
        clickOn(playerOneOption);
        Node playerTwoOption = lookup("#player-two .config-option").match(optionWithText("Human")).query();
        clickOn(playerTwoOption);
        Platform.runLater(() -> isValidGameConfig.set(view.validateGameConfig()));

        waitForFxEvents();
        assertTrue(isValidGameConfig.get());
    }

    @Test
    public void validateGameConfig_whenAllConfigSelected_shouldNotDisplayErrorMessage() {
        DisplayGameConfig gameConfig = new DisplayGameConfig();
        gameConfig.playerTypes = DISPLAY_PLAYER_TYPES;

        view.displayWelcome();
        view.displayGameConfig(gameConfig);
        Platform.runLater(() -> view.validateGameConfig());
        waitForFxEvents();
        Node playerOneOption = lookup("#player-one .config-option").match(optionWithText("Human")).query();
        clickOn(playerOneOption);
        Node playerTwoOption = lookup("#player-two .config-option").match(optionWithText("Human")).query();
        clickOn(playerTwoOption);
        Platform.runLater(() -> view.validateGameConfig());

        waitForFxEvents();
        verifyThat("#player-one .error-message", isInvisible());
        verifyThat("#player-two .error-message", isInvisible());
    }

    private Predicate<RadioButton> optionWithText(String targetText) {
        return (RadioButton rb) -> rb.getText().equals(targetText);
    }

    private DisplayBoard createBoard(String... cells) {
        DisplayBoard board = new DisplayBoard();
        for (int i = 0; i < cells.length; i++)
            board.cells.add(new DisplayCell(cells[i], i, xPos(i), yPos(i)));
        return board;
    }

    private int xPos(int i) {
        return i % BOARD_SIZE;
    }

    private int yPos(int i) {
        return i / BOARD_SIZE;
    }

    private void assertGridPosition(String cellIdSelector, int columnIdx, int rowIdx) {
        Label cell = lookup(cellIdSelector).query();
        assertEquals(columnIdx, GridPane.getColumnIndex(cell).intValue());
        assertEquals(rowIdx, GridPane.getRowIndex(cell).intValue());
    }

    private DisplayBoard createBoardWithTriggerHandlerCells(List<Integer> triggeredCells) {
        DisplayBoard board = new DisplayBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            DisplayCell cell = new DisplayCell(" ", i, xPos(i), yPos(i));
            final int cellNum = i;
            cell.actionHandler = () -> triggeredCells.add(cellNum);
            board.cells.add(cell);
        }
        return board;
    }

    private void clickOnAllCells() {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
            clickOn("#cell-" + i);
    }

    private void displayAndClickOnAllCells(DisplayBoard board) {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            view.displayBoard(board);
            waitForFxEvents();
            clickOn("#cell-" + i);
        }
    }

    private List<Integer> listOfIntegersInRange(int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).boxed().collect(Collectors.toList());
    }

    private void assertOptionsHasText(String lookupQuery, List<String> expectedOptions) {
        Set<Node> nodes = lookup(lookupQuery).queryAll();
        for (Node node : nodes)
            expectedOptions.contains(((RadioButton) node).getText());
    }
}
