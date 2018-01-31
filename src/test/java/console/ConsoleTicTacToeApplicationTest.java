package console;

import application.MockPlayerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ConsoleTicTacToeApplicationTest {
    private static final String[] DEFAULT_INPUTS = {"1", "1", "n"};
    private static final int[] DEFAULT_PLAYER_ONE_MOVES = {0, 1, 2};
    private static final int[] DEFAULT_PLAYER_TWO_MOVES = {3, 4};
    private static final Queue<int[]> DEFAULT_MOVES_QUEUE =
            new ArrayDeque<>(Arrays.asList(DEFAULT_PLAYER_ONE_MOVES, DEFAULT_PLAYER_TWO_MOVES));
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ConsoleTicTacToeApplication application;
    private MockPlayerFactory mockPlayerFactory;

    @Test
    public void shouldPrintWelcomeMessage() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("===== Welcome to Tic-Tac-Toe =====");
    }

    @Test
    public void shouldPromptPlayerOneType() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("Select player one (X) type (1 - Human, 2 - Computer): ");
    }

    @Test
    public void shouldPromptPlayerTwoType() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("Select player two (O) type (1 - Human, 2 - Computer): ");
    }

    @Test
    public void shouldCreateCorrectTypes_playerOneHuman_playerTwoComputer() throws Exception {
        runWithInput(new String[]{"1", "2", "n"}, DEFAULT_MOVES_QUEUE);
        assertPlayerTypes("consoleHuman", "computer");
    }

    @Test
    public void shouldCreateCorrectTypes_playerOneComputer_playerTwoHuman() throws Exception {
        runWithInput(new String[]{"2", "1", "n"}, DEFAULT_MOVES_QUEUE);
        assertPlayerTypes("computer", "consoleHuman");
    }

    @Test
    public void nonIntegerPlayerTypeInput_shouldDisplayError() throws Exception {
        runWithInput(new String[]{"W", "1", "2", "n"}, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("Invalid input. Please enter a valid number.");
    }

    @Test
    public void nonExistingPlayerTypeInput_shouldDisplayError() throws Exception {
        runWithInput(new String[]{"6", "1", "2", "n"}, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("Invalid input. Please select a valid player type.");
    }

    @Test
    public void shouldCreateCorrectMarkers_playerOneCross_playerTwoCircle() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertEquals(2, mockPlayerFactory.playerMarkers.size());
        assertEquals('X', (char) mockPlayerFactory.playerMarkers.get(0));
        assertEquals('O', (char) mockPlayerFactory.playerMarkers.get(1));
    }

    @Test
    public void shouldCreateCorrectOpponentMarkers_playerOneOppCircle_playerTwoOppCross() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertEquals(2, mockPlayerFactory.opponentMarkers.size());
        assertEquals('O', (char) mockPlayerFactory.opponentMarkers.get(0));
        assertEquals('X', (char) mockPlayerFactory.opponentMarkers.get(1));
    }

    @Test
    public void shouldPromptPlayAgain() throws Exception {
        runWithInput(DEFAULT_INPUTS, DEFAULT_MOVES_QUEUE);
        assertOutputStreamContains("Would you like to play again (y/n)? ");
    }

    @Test
    public void shouldAllowMultiplePlayAgain() throws Exception {
        Queue<int[]> playerMovesQueue = new ArrayDeque<>();
        playerMovesQueue.add(DEFAULT_PLAYER_ONE_MOVES);
        playerMovesQueue.add(DEFAULT_PLAYER_TWO_MOVES);
        playerMovesQueue.add(DEFAULT_PLAYER_ONE_MOVES);
        playerMovesQueue.add(DEFAULT_PLAYER_TWO_MOVES);
        playerMovesQueue.add(DEFAULT_PLAYER_ONE_MOVES);
        playerMovesQueue.add(DEFAULT_PLAYER_TWO_MOVES);
        runWithInput(new String[]{"1", "1", "y", "1", "1", "y", "1", "1", "n"}, playerMovesQueue);

        assertEquals(3, countOf("===== Welcome to Tic-Tac-Toe =====", contentsOf(outputStream)));
        assertEquals(3, countOf("X has Won!", contentsOf(outputStream)));
        assertEquals(3, countOf("Would you like to play again (y/n)? ", contentsOf(outputStream)));
    }

    private void runWithInput(String[] appInputs, Queue<int[]> playerMovesQueue) {
        String inputStr = InputTestUtil.toInputString(appInputs);
        runUsing(inputStr, playerMovesQueue);
    }

    private void runUsing(String inputStr, Queue<int[]> playerMovesQueue) {
        inputStream = new ByteArrayInputStream(
                String.format(inputStr).getBytes(StandardCharsets.UTF_8));
        outputStream = new ByteArrayOutputStream();
        mockPlayerFactory = new MockPlayerFactory();
        mockPlayerFactory.playerMovesQueue.addAll(playerMovesQueue);
        application = new ConsoleTicTacToeApplication(inputStream, new PrintStream(outputStream), mockPlayerFactory);
        application.run();
    }

    private void assertOutputStreamContains(String expectedString) throws UnsupportedEncodingException {
        assertThat(contentsOf(outputStream), containsString(expectedString));
    }

    private String contentsOf(ByteArrayOutputStream outputStream) throws UnsupportedEncodingException {
        return outputStream.toString(StandardCharsets.UTF_8.name());
    }

    private int countOf(String substring, String string) {
        final int NO_LIMIT_KEEP_TRAILING_EMPTY_STRINGS = -1;
        return string.split(Pattern.quote(substring), NO_LIMIT_KEEP_TRAILING_EMPTY_STRINGS).length - 1;
    }

    private void assertPlayerTypes(String playerOneType, String playerTwoType) {
        assertEquals(2, mockPlayerFactory.playerTypesCreated.size());
        assertEquals(playerOneType, mockPlayerFactory.playerTypesCreated.get(0));
        assertEquals(playerTwoType, mockPlayerFactory.playerTypesCreated.get(1));
    }
}