package players;

import application.PlayerFactory;
import game.Board;
import game.Player;

import java.io.InputStream;
import java.io.PrintStream;

public class DefaultPlayerFactory implements PlayerFactory {
    private final InputStream inputStream;
    private final PrintStream printStream;

    public DefaultPlayerFactory(InputStream inputStream, PrintStream printStream) {
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

    @Override
    public Player createPlayer(String type, char marker, Board board, char opponentMarker) {
        if ("human".equals(type))
            return new HumanPlayer(marker, board, this.inputStream, this.printStream);
        else if ("computer".equals(type))
            return new ComputerPlayer(marker, board, opponentMarker);
        else
            throw new IllegalArgumentException("Invalid player type: " + type);
    }
}
