package application;

import game.Board;
import game.Player;
import game.TestPlayer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MockPlayerFactory implements PlayerFactory {
    public Queue<int[]> playerMovesQueue = new ArrayDeque<>();
    public List<String> playerTypesCreated = new ArrayList<>();
    public List<Character> playerMarkers = new ArrayList<>();
    public List<Character> opponentMarkers = new ArrayList<>();
    public List<Board> boardsPassedToCreatePlayer = new ArrayList<>();

    @Override
    public Player createPlayer(String type, char marker, Board board, char opponentMarker) {
        this.playerTypesCreated.add(type);
        this.playerMarkers.add(marker);
        this.boardsPassedToCreatePlayer.add(board);
        this.opponentMarkers.add(opponentMarker);
        return createTestPlayerWithNextMoves(marker, board);
    }

    private Player createTestPlayerWithNextMoves(char marker, Board board) {
        TestPlayer player = new TestPlayer(marker, board);
        player.enqueueMoves(this.playerMovesQueue.remove());
        return player;
    }
}
