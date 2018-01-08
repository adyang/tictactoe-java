package game;

import java.util.ArrayList;
import java.util.List;

public class ComputerPlayer implements Player {
    private final char marker;
    private final Board board;
    private final char opponentMarker;

    public ComputerPlayer(char marker, Board board, char opponentMarker) {
        this.marker = marker;
        this.board = board;
        this.opponentMarker = opponentMarker;
    }

    @Override
    public char getMarker() {
        return this.marker;
    }

    @Override
    public void makeMove() {
        List<Integer> moves = possibleMoves(board);
        int bestMoveSoFar = moves.get(0);
        int bestValueSoFar = Integer.MIN_VALUE;
        for (int move : moves) {
            Board draftBoard = playDraftMove(board, move, this.getMarker());
            int currValue = minValue(draftBoard, 0);
            if (currValue > bestValueSoFar) {
                bestValueSoFar = currValue;
                bestMoveSoFar = move;
            }
        }
        board.mark(bestMoveSoFar, getMarker());
    }

    private Board playDraftMove(Board board, int move, char marker) {
        Board draftBoard = board.copy();
        draftBoard.mark(move, marker);
        return draftBoard;
    }

    private int minValue(Board board, int depth) {
        if (board.hasEnded())
            return utilityOf(board, depth);

        int minSoFar = Integer.MAX_VALUE;
        for (Integer move : possibleMoves(board)) {
            Board draftBoard = playDraftMove(board, move, opponentMarker);
            minSoFar = Math.min(maxValue(draftBoard, depth + 1 ), minSoFar);
        }
        return minSoFar;
    }

    private int maxValue(Board board, int depth) {
        if (board.hasEnded())
            return utilityOf(board, depth);

        int maxSoFar = Integer.MIN_VALUE;
        for (Integer move : possibleMoves(board)) {
            Board draftBoard = playDraftMove(board, move, getMarker());
            maxSoFar = Math.max(minValue(draftBoard, depth + 1), maxSoFar);
        }
        return maxSoFar;
    }

    private int utilityOf(Board board, int depth) {
        if (board.hasWinner() && board.getWinner() == this.getMarker())
            return 1000 - depth;
        else if (board.hasWinner())
            return depth - 1000;
        else
            return 0;
    }

    private List<Integer> possibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < board.size() * board.size(); i++) {
            if (!board.isMarked(i))
                possibleMoves.add(i);
        }
        return possibleMoves;
    }
}
