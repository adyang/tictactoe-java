package players;

import boards.ThreeByThreeBoard;
import console.PlayerFactory;
import game.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultPlayerFactoryTest {

    private PlayerFactory playerFactory;
    private ThreeByThreeBoard board;

    @Before
    public void setUp() throws Exception {
        playerFactory = new DefaultPlayerFactory(System.in, System.out);
        board = new ThreeByThreeBoard();
    }

    @Test
    public void createHumanPlayer() {
        Player player = playerFactory.createPlayer("human", 'X', board, 'O');
        assertTrue(player instanceof HumanPlayer);
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
