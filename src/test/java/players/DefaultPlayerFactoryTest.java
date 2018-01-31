package players;

import application.PlayerFactory;
import boards.ThreeByThreeBoard;
import game.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultPlayerFactoryTest {
    private PlayerFactory playerFactory;
    private ThreeByThreeBoard board;

    @Before
    public void setUp() throws Exception {
        playerFactory = new DefaultPlayerFactory(System.in, System.out, new ArrayBlockingQueue<>(1));
        board = new ThreeByThreeBoard();
    }

    @Test
    public void createConsoleHumanPlayer() {
        Player player = playerFactory.createPlayer("consoleHuman", 'X', board, 'O');
        assertTrue(player instanceof ConsoleHumanPlayer);
        assertEquals('X', player.getMarker());
    }

    @Test
    public void createGuiHumanPlayer() {
        Player player = playerFactory.createPlayer("guiHuman", 'X', board, 'O');
        assertTrue(player instanceof GuiHumanPlayer);
        assertEquals('X', player.getMarker());
    }

    @Test
    public void createComputerPlayer() {
        Player player = playerFactory.createPlayer("computer", 'X', board, 'O');
        assertTrue(player instanceof ComputerPlayer);
        assertEquals('X', player.getMarker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidType_shouldThrowException() {
        playerFactory.createPlayer("invalidType", 'X', board, 'O');
    }
}
