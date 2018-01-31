package gui;

import application.MockPlayerFactory;
import application.PlayerNumber;
import boards.ThreeByThreeBoard;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;

public class GuiTicTacToeApplicationTest {
    private GuiTicTacToeApplication application;
    private BlockingQueue<String> playerTypeQueue;
    private BlockingQueue<Boolean> playAgainQueue;
    private MockView mockView;
    private MockPlayerFactory mockPlayerFactory;

    @Before
    public void setUp() throws Exception {
        playerTypeQueue = new ArrayBlockingQueue<>(PlayerNumber.values().length);
        playAgainQueue = new ArrayBlockingQueue<>(1);
        mockView = new MockView();
        mockPlayerFactory = new MockPlayerFactory();
        application = new GuiTicTacToeApplication(playerTypeQueue, playAgainQueue, null, mockView, mockPlayerFactory);
    }

    @Test
    public void displayWelcome_shouldDelegateViewToDisplayWelcome() {
        application.displayWelcome();

        assertTrue(mockView.displayWelcomeCalled);
    }

    @Test
    public void displayWelcome_shouldDisplayGameConfigOptionsForPlayerType() {
        application.displayWelcome();

        assertEquals(Arrays.asList("Human", "Computer"), mockView.displayGameConfig.playerTypes);
    }

    @Test
    public void displayWelcome_whenRunPlayActionHandler_shouldAddSelectedPlayerTypeToQueue() {
        application.displayWelcome();
        mockView.isValidGameConfig = true;
        mockView.playerOneSelectedType = "Human";
        mockView.playerTwoSelectedType = "Computer";

        mockView.displayGameConfig.playHandler.run();

        assertEquals(2, playerTypeQueue.size());
        assertEquals("guiHuman", playerTypeQueue.remove());
        assertEquals("computer", playerTypeQueue.remove());
    }

    @Test
    public void displayWelcome_whenRunPlayActionHandler_OnInvalidConfig_shouldNotAddToQueue() {
        application.displayWelcome();
        mockView.isValidGameConfig = false;

        mockView.displayGameConfig.playHandler.run();

        assertEquals(0, playerTypeQueue.size());
    }

    @Test
    public void configurePlayer_shouldCreateCorrectPlayerTypes() {
        playerTypeQueue.add("guiHuman");
        playerTypeQueue.add("computer");
        mockPlayerFactory.playerMovesQueue.addAll(Arrays.asList(new int[0], new int[0]));

        application.configurePlayer(PlayerNumber.ONE, null);
        application.configurePlayer(PlayerNumber.TWO, null);

        assertEquals("guiHuman", mockPlayerFactory.playerTypesCreated.get(0));
        assertEquals("computer", mockPlayerFactory.playerTypesCreated.get(1));
    }

    @Test
    public void configurePlayer_shouldCreatePlayerWithCorrectMarkersAndBoard() {
        playerTypeQueue.add("guiHuman");
        playerTypeQueue.add("computer");
        mockPlayerFactory.playerMovesQueue.addAll(Arrays.asList(new int[0], new int[0]));

        ThreeByThreeBoard board = new ThreeByThreeBoard();
        application.configurePlayer(PlayerNumber.ONE, board);
        application.configurePlayer(PlayerNumber.TWO, board);

        assertSame(board, mockPlayerFactory.boardsPassedToCreatePlayer.get(0));
        assertSame(board, mockPlayerFactory.boardsPassedToCreatePlayer.get(1));
        assertEquals(PlayerNumber.ONE.marker, mockPlayerFactory.playerMarkers.get(0).charValue());
        assertEquals(PlayerNumber.TWO.marker, mockPlayerFactory.playerMarkers.get(1).charValue());
        assertEquals(PlayerNumber.ONE.opponentMarker, mockPlayerFactory.opponentMarkers.get(0).charValue());
        assertEquals(PlayerNumber.TWO.opponentMarker, mockPlayerFactory.opponentMarkers.get(1).charValue());
    }

    @Test
    public void displayPlayAgain_whenRunPlayAgainHandler_shouldAddToPlayAgainQueue() throws InterruptedException {
        playAgainQueue.add(false);
        application.playAgain();

        mockView.bindedPlayAgainHandler.run();

        assertTrue(playAgainQueue.take());
    }

    @Test
    public void displayPlayAgain_whenPlayAgainResponseReceived_shouldHidePlayAgainView() throws InterruptedException {
        playAgainQueue.add(true);

        application.playAgain();

        assertTrue(mockView.hidePlayAgainCalled);
    }
}
