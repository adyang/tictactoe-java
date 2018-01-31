package players;

import application.PlayerFactory;
import game.Board;
import game.Player;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;

public class DefaultPlayerFactory implements PlayerFactory {
    private final InputStream inputStream;
    private final PrintStream printStream;
    private final BlockingQueue<Integer> moveQueue;

    public DefaultPlayerFactory(InputStream inputStream, PrintStream printStream) {
        this(inputStream, printStream, null);
    }

    public DefaultPlayerFactory(InputStream inputStream, PrintStream printStream, BlockingQueue<Integer> moveQueue) {
        this.inputStream = inputStream;
        this.printStream = printStream;
        this.moveQueue = moveQueue;
    }

    @Override
    public Player createPlayer(String type, char marker, Board board, char opponentMarker) {
        if ("consoleHuman".equals(type))
            return new ConsoleHumanPlayer(marker, board, this.inputStream, this.printStream);
        else if ("guiHuman".equals(type))
            return createGuiHumanPlayer(marker, board, moveQueue);
        else if ("computer".equals(type))
            return new ComputerPlayer(marker, board, opponentMarker);
        else
            throw new IllegalArgumentException("Invalid player type: " + type);
    }

    private GuiHumanPlayer createGuiHumanPlayer(char marker, Board board, BlockingQueue<Integer> moveQueue) {
        if (moveQueue == null)
            throw new IllegalStateException("Unable to create Gui Human: moveQueue is not set for Factory.");
        return new GuiHumanPlayer(marker, board, moveQueue);
    }
}
