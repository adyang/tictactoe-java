package game;

import java.util.ArrayDeque;
import java.util.Queue;

public class TestPlayer implements Player {
    int makeMoveCount;
    Queue<Integer> movesQueue;
    private char marker;
    private Board board;

    public TestPlayer(char marker, Board board) {
        this.movesQueue = new ArrayDeque<>();
        this.marker = marker;
        this.board = board;
    }

    public void enqueueMoves(int... moves) {
        for (int m : moves)
            movesQueue.add(m);
    }

    @Override
    public char getMarker() {
        return marker;
    }

    @Override
    public void makeMove() {
        makeMoveCount++;
        board.mark(movesQueue.remove(), marker);
    }
}
