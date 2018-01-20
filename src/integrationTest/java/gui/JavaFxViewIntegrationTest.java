package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static gui.DisplayBoard.DisplayCell;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class JavaFxViewIntegrationTest extends ApplicationTest {
    private static final int BOARD_SIZE = 3;
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
}
