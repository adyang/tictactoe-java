package game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerPlayerTest {

    private Board board;
    private Player computer;

    @Before
    public void setUp() throws Exception {
        board = new ThreeByThreeBoard();
        computer = new ComputerPlayer('O', board, 'X');
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onePossibleMoveLeftAtPosSeven_shouldSelectThatMove() {
        setupBoard('O', 'X', 'O',
                   'O', 'O', 'X',
                   'X', ' ', 'X');
        computer.makeMove();
        assertEquals('O', board.getStatus()[7]);

    }

    @Test
    public void onePossibleMoveLeftAtPosEight_shouldSelectThatMove() {
        setupBoard('O', 'X', 'O',
                   'O', 'O', 'X',
                   'X', 'X', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[8]);
    }

    @Test
    public void twoPossibleMoves_drawAndLose_shouldSelectDrawMove() {
        setupBoard('X', 'O', 'X',
                   'O', 'X', 'X',
                   ' ', ' ', 'O');
        computer.makeMove();
        assertEquals('O', board.getStatus()[6]);
        assertEquals('\0', board.getStatus()[7]);
    }

    @Test
    public void twoPossibleMoves_winAndLose_shouldSelectWinMove() {
        setupBoard('O', 'X', 'X',
                   'X', 'O', 'X',
                   'O', ' ', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[8]);
        assertEquals('\0', board.getStatus()[7]);
    }

    @Test
    public void twoPossibleMoves_winAndDraw_shouldSelectWinMove() {
        setupBoard('X', 'O', ' ',
                   'X', 'O', 'X',
                   'O', 'X', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[2]);
        assertEquals('\0', board.getStatus()[8]);
    }

    @Test
    public void winMoveAvailable_shouldSelectWinMove() {
        setupBoard('X', 'O', ' ',
                   'X', 'O', ' ',
                   ' ', ' ', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[7]);
        assertEmptyCells(2, 5, 6, 8);
    }

    @Test
    public void opponentWinsInNextMove_shouldBlockOpponent() {
        setupBoard('X', 'O', ' ',
                   'X', ' ', ' ',
                   ' ', ' ', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[6]);
        assertEmptyCells(2, 4, 5, 7, 8);
    }

    @Test
    public void opportunityToSetupTrap_shouldSetupDoubleWinTrap() {
        setupBoard('O', 'X', ' ',
                   ' ', ' ', 'X',
                   ' ', 'O', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[6]);
    }

    @Test
    public void opponentOpenWithCenter_shouldSelectCornerMove() {
        setupBoard(' ', ' ', ' ',
                   ' ', 'X', ' ',
                   ' ', ' ', ' ');
        computer.makeMove();
        assertTrue(hasMarkerAtAnyPositions('O', 0, 2, 6, 8));
    }

    @Test
    public void opponentOpenWithCorner_shouldSelectCenterMove() {
        setupBoard('X', ' ', ' ',
                   ' ', ' ', ' ',
                   ' ', ' ', ' ');
        computer.makeMove();
        assertEquals('O', board.getStatus()[4]);
    }

    @Test
    public void initialMove_shouldSelectCenterOrCornerMove() {
        setupBoard(' ', ' ', ' ',
                   ' ', ' ', ' ',
                   ' ', ' ', ' ');
        computer.makeMove();
        assertTrue(hasMarkerAtAnyPositions('O', 4, 0, 2, 6, 8));
    }

    @Test
    public void computerWithMarkerX_shouldProlongLosingGame() {
        computer = new ComputerPlayer('X', board, 'O');
        setupBoard(' ', 'O', ' ',
                   ' ', ' ', 'O',
                   'X', 'X', 'O');
        computer.makeMove();
        assertEquals('X', board.getStatus()[2]);
    }

    private void setupBoard(char... cells) {
        for (int i = 0; i < cells.length; i++)
            if (cells[i] != ' ')
                board.mark(i, cells[i]);
    }

    private void assertEmptyCells(int... expectedEmptyPositions) {
        for (int pos : expectedEmptyPositions)
            assertEquals('\0', board.getStatus()[pos]);
    }

    private boolean hasMarkerAtAnyPositions(char expectedMarker, int... positions) {
        for (int pos : positions) {
            if (board.getStatus()[pos] == expectedMarker)
                return true;
        }
        return false;
    }
}