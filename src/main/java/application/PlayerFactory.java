package application;

import game.Board;
import game.Player;

public interface PlayerFactory {
    Player createPlayer(String type, char marker, Board board, char opponentMarker);
}
