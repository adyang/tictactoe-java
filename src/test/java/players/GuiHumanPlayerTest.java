package players;

import boards.ThreeByThreeBoard;
import game.Board;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;

public class GuiHumanPlayerTest {
    private Board board;
    private BlockingQueue<Integer> moveQueue;
    private GuiHumanPlayer player;

    @Before
    public void setUp() throws Exception {
        board = new ThreeByThreeBoard();
        moveQueue = new ArrayBlockingQueue<>(1);
        player = new GuiHumanPlayer('X', board, moveQueue);
    }

    @Test
    public void shouldPlayMoveFromQueue() {
        moveQueue.add(5);

        player.makeMove();

        assertEquals('X', board.getStatus()[5]);
    }
}
