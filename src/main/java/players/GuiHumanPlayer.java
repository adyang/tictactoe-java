package players;

import game.Board;
import game.Player;

import java.util.concurrent.BlockingQueue;

public class GuiHumanPlayer implements Player {
    private final char marker;
    private final Board board;
    private final BlockingQueue<Integer> moveQueue;

    public GuiHumanPlayer(char marker, Board board, BlockingQueue<Integer> moveQueue) {
        this.marker = marker;
        this.board = board;
        this.moveQueue = moveQueue;
    }

    @Override
    public char getMarker() {
        return this.marker;
    }

    @Override
    public void makeMove() {
        try {
            int selectedMove = moveQueue.take();
            board.mark(selectedMove, getMarker());
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for move input.", e);
        }
    }
}
