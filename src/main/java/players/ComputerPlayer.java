package players;

import game.Board;
import game.Player;

import java.util.List;

public class ComputerPlayer implements Player {
    public static final int STARTING_DEPTH = 0;
    public static final int LOSE_VALUE = -1000;
    public static final int WIN_VALUE = 1000;
    public static final int DRAW_VALUE = 0;
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
        board.mark(miniMaxDecision(), this.getMarker());
    }

    private int miniMaxDecision() {
        List<Integer> moves = board.possibleMoves();
        int bestMoveSoFar = moves.get(0);
        int bestValueSoFar = Integer.MIN_VALUE;
        for (int move : moves) {
            Board draftBoard = playDraftMove(board, move, this.getMarker());
            int currValue = miniMaxValue(draftBoard, STARTING_DEPTH, opponentMarker);
            if (currValue > bestValueSoFar) {
                bestValueSoFar = currValue;
                bestMoveSoFar = move;
            }
        }
        return bestMoveSoFar;
    }

    private Board playDraftMove(Board board, int move, char marker) {
        Board draftBoard = board.copy();
        draftBoard.mark(move, marker);
        return draftBoard;
    }

    private int miniMaxValue(Board board, int depth, char playerMarker) {
        if (board.hasEnded())
            return finalValueOf(board, depth);

        return bestValueForPlayer(board, depth, playerMarker);
    }

    private int finalValueOf(Board board, int depth) {
        if (board.hasWinner() && isThisPlayer(board.getWinner()))
            return lessValueIfLongerToWin(depth);
        else if (board.hasWinner())
            return moreValueIfLongerToLose(depth);
        else
            return DRAW_VALUE;
    }

    private boolean isThisPlayer(char marker) {
        return marker == this.getMarker();
    }

    private int lessValueIfLongerToWin(int turnsToWin) {
        return WIN_VALUE - turnsToWin;
    }

    private int moreValueIfLongerToLose(int turnsToLose) {
        return LOSE_VALUE + turnsToLose;
    }

    private int bestValueForPlayer(Board board, int depth, char playerMarker) {
        if (isThisPlayer(playerMarker))
            return thisPlayerBestValue(board, depth);
        else
            return opponentBestValue(board, depth);
    }

    private int thisPlayerBestValue(Board board, int depth) {
        int maxSoFar = Integer.MIN_VALUE;
        for (Integer move : board.possibleMoves()) {
            Board draftBoard = playDraftMove(board, move, this.getMarker());
            maxSoFar = Math.max(miniMaxValue(draftBoard, depth + 1, opponentMarker), maxSoFar);
        }
        return maxSoFar;
    }

    private int opponentBestValue(Board board, int depth) {
        int minSoFar = Integer.MAX_VALUE;
        for (Integer move : board.possibleMoves()) {
            Board draftBoard = playDraftMove(board, move, opponentMarker);
            minSoFar = Math.min(miniMaxValue(draftBoard, depth + 1, this.getMarker()), minSoFar);
        }
        return minSoFar;
    }
}
