package gui;

import game.Board;
import game.Game;
import game.Player;
import gui.DisplayBoard.DisplayCell;

import java.util.concurrent.BlockingQueue;

public class GuiGame extends Game {
    private final View view;
    private final BlockingQueue<Integer> moveQueue;

    public GuiGame(Board board, Player playerOne, Player playerTwo, View view, BlockingQueue<Integer> moveQueue) {
        super(board, playerOne, playerTwo);
        this.view = view;
        this.moveQueue = moveQueue;
    }

    @Override
    protected void displayBoardStatus() {
        DisplayBoard displayBoard = toDisplayBoard(board);
        view.displayBoard(displayBoard);
    }

    private DisplayBoard toDisplayBoard(Board board) {
        DisplayBoard displayBoard = new DisplayBoard();
        char[] cells = board.getStatus();
        for (int i = 0; i < cells.length; i++) {
            displayBoard.cells.add(newDisplayCell(i, cells));
        }
        return displayBoard;
    }

    private DisplayCell newDisplayCell(int i, char[] cells) {
        DisplayCell displayCell = new DisplayCell(cellAt(i, cells), i, xPos(i), yPos(i));
        displayCell.actionHandler = board.isMarked(i) ? null : cellHandlerFor(i);
        return displayCell;
    }

    private String cellAt(int i, char[] cells) {
        if (cells[i] == '\0')
            return " ";
        else
            return String.valueOf(cells[i]);
    }

    private int xPos(int i) {
        return i % board.size();
    }

    private int yPos(int i) {
        return i / board.size();
    }

    private Runnable cellHandlerFor(int position) {
        return () -> {
            validateAction(position);
            tryAddMoveToQueue(position);
        };
    }

    private void validateAction(int position) {
        if (board.isMarked(position))
            throw new IllegalStateException("Cell at position " + position + " already marked.");
    }

    private void tryAddMoveToQueue(int position) {
        try {
            moveQueue.add(position);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Pending move already on queue: " + moveQueue.peek(), e);
        }
    }

    @Override
    protected void displayCurrentTurn(Player currentPlayer) {
        view.displayCurrentMarker(String.valueOf(currentPlayer.getMarker()));
    }

    @Override
    protected void displayEndStatus() {
        if (board.hasWinner())
            view.displayWinner(String.valueOf(board.getWinner()));
        else
            view.displayDraw();
    }
}
