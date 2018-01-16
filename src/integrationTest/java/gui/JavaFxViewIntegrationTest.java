package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static gui.DisplayBoard.*;
import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

public class JavaFxViewIntegrationTest extends ApplicationTest {
    private static final int BOARD_SIZE = 3;
    private View view;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        Group dummyGroup = new Group();
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

        Label cell = lookup("#cell-5").query();
        verifyThat("#cell-5", hasText("X"));
        assertGridPosition(cell, 2, 1);
        cell = lookup("#cell-6").query();
        verifyThat("#cell-6", hasText("O"));
        assertGridPosition(cell, 0, 2);
    }

    private void assertGridPosition(Label cell, int expectedColumnIdx, int expectedRowIdx) {
        assertEquals(expectedColumnIdx, GridPane.getColumnIndex(cell).intValue());
        assertEquals(expectedRowIdx, GridPane.getRowIndex(cell).intValue());
    }

    private DisplayBoard createBoard(String... cells) {
        DisplayBoard board = new DisplayBoard();
        for (int y = 0, cellIdx = 0; y < BOARD_SIZE; y++)
            for (int x = 0; x < BOARD_SIZE; x++)
                board.cells.add(new DisplayCell(cells[cellIdx++], x, y));
        return board;
    }
}
