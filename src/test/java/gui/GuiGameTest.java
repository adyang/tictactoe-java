package gui;

import boards.ThreeByThreeBoard;
import game.Board;
import game.TestPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import static gui.DisplayBoard.DisplayCell;
import static org.junit.Assert.*;

public class GuiGameTest {
    private Board board;
    private MockView mockView;
    private GuiGame game;
    private BlockingQueue<Integer> moveQueue;
    private TestPlayer player;

    @Before
    public void setUp() throws Exception {
        board = new ThreeByThreeBoard();
        mockView = new MockView();
        moveQueue = new ArrayBlockingQueue<>(1);
        player = new TestPlayer('X', board);
        game = new GuiGame(board, player, null, mockView, moveQueue);
    }

    @Test
    public void displayBoardStatus_shouldViewDisplayBoardWithEmptyCells() {
        game.displayBoardStatus();

        assertCellsHaveMarker(" ", IntStream.range(0, 9).toArray());
    }

    @Test
    public void displayBoardStatus_shouldViewDisplayBoardWithMarkedCells() {
        markBoardAtPositions('X', 0, 1, 2);

        game.displayBoardStatus();

        assertCellsHaveMarker("X", 0, 1, 2);
    }

    @Test
    public void displayBoardStatus_shouldSetActionHandlerOnCells() {
        game.displayBoardStatus();

        DisplayBoard displayBoard = mockView.displayBoard;
        for (DisplayCell cell : displayBoard.cells) {
            cell.actionHandler.run();
            int move = moveQueue.remove();
            assertEquals(cell.idxPos, move);
        }
    }

    @Test
    public void displayBoardStatus_whenCellAlreadyMarked_shouldHaveNullActionHandler() {
        board.mark(0, 'X');
        game.displayBoardStatus();

        DisplayCell displayCell = mockView.displayBoard.cells.get(0);
        assertNull(displayCell.actionHandler);
    }

    @Test
    public void displayBoardStatus_whenCellActivatedAndMoveIsStillOnQueue_shouldThrowException() {
        game.displayBoardStatus();

        try {
            DisplayCell displayCell = mockView.displayBoard.cells.get(0);
            displayCell.actionHandler.run();
            displayCell.actionHandler.run();
            fail("Should throw exception on activating cell when move is still on queue.");
        } catch (IllegalStateException e) {
            assertEquals("Pending move already on queue: 0", e.getMessage());
            assertEquals(1, moveQueue.size());
        }
    }

    @Test
    public void displayCurrentTurn_shouldDelegateMarkerToView() {
        game.displayCurrentTurn(player);

        assertEquals("X", mockView.displayedMarker);
    }

    @Test
    public void displayEndStatus_shouldDelegateWinnerToView() {
        markBoardAtPositions('X', 0, 1, 2);
        markBoardAtPositions('O', 3, 4);

        game.displayEndStatus();

        assertEquals("X", mockView.displayedWinner);
    }

    @Test
    public void displayEndStatus_shouldDelegateDrawStatusToView() {
        markBoardAtPositions('X', 0, 1, 4, 5, 6);
        markBoardAtPositions('O', 2, 3, 7, 8);

        game.displayEndStatus();

        assertTrue(mockView.displayDrawCalled);
    }

    @Test
    public void displayEndStatus_shouldDisplayEndStatusBoard() {
        markBoardAtPositions('X', 0, 1, 4, 5, 6);
        markBoardAtPositions('O', 2, 3, 7, 8);

        game.displayEndStatus();

        assertCellsHaveMarker("X", 0, 1, 4, 5, 6);
        assertCellsHaveMarker("O", 2, 3, 7, 8);
    }

    private void assertCellsHaveMarker(String expectedMarker, int... expectedPositions) {
        DisplayBoard displayBoard = mockView.displayBoard;
        for (DisplayCell cell: displayBoard.cells) {
            if (Arrays.asList(expectedPositions).contains(cell.idxPos))
                assertEquals(expectedMarker, cell.marker);
        }
    }

    private void markBoardAtPositions(char marker, int... positions) {
        for (int pos : positions)
            board.mark(pos, marker);
    }

    private class MockView implements View {
        DisplayBoard displayBoard;
        String displayedMarker;
        String displayedWinner;
        boolean displayDrawCalled;

        @Override
        public void displayBoard(DisplayBoard displayBoard) {
            this.displayBoard = displayBoard;
        }

        @Override
        public void displayCurrentMarker(String marker) {
            this.displayedMarker = marker;
        }

        @Override
        public void displayWinner(String winner) {
            this.displayedWinner = winner;
        }

        @Override
        public void displayDraw() {
            this.displayDrawCalled = true;
        }
    }
}