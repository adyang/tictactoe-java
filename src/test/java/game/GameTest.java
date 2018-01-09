package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private TestBoard board;
	private TestPlayer playerTwo;
	private TestPlayer playerOne;
	private TestGame game;

	@Before
	public void setUp() throws Exception {
		setUpTestGame();
		setUpPlayerMoves();
	}

	private void setUpTestGame() {
		board = new TestBoard();
		playerOne = new TestPlayer('X', board);
		playerTwo = new TestPlayer('O', board);
		game = new TestGame(board, playerOne, playerTwo);
	}

	private void setUpPlayerMoves() {
		playerOne.enqueueMoves(1, 2, 5);
		playerTwo.enqueueMoves(3, 4);
	}

	@Test
	public void printWelcome() throws Exception {
		board.turnsToRun = 0;
		game.start();
		assertTrue(game.printWelcomeCalled);
	}

	@Test
	public void boardEnds() throws Exception {
		board.turnsToRun = 0;
		game.start();
		assertTrue(game.printEndStatusCalled);
	}

	@Test
	public void oneTurn_printBoardStatusOnce() throws Exception {
		board.turnsToRun = 1;
		game.start();
		assertEquals(1, game.printBoardStatusCount);
	}

	@Test
	public void threeTurns_printBoardStatusThrice() throws Exception {
		board.turnsToRun = 3;
		game.start();
		assertEquals(3, game.printBoardStatusCount);
	}

	@Test
	public void printTurnPlayer() throws Exception {
		board.turnsToRun = 1;
		game.start();
		assertEquals(playerOne, game.lastPlayerPrinted);
	}

	@Test
	public void playersAlternateTurns() throws Exception {
		board.turnsToRun = 5;
		game.start();
		assertEquals(3, playerOne.makeMoveCount);
		assertEquals(2, playerTwo.makeMoveCount);
		assertEquals(Arrays.asList(1, 3, 2, 4, 5), board.markedPositionsInOrder);
	}

	private static class TestGame extends Game {
		Player lastPlayerPrinted;
		boolean printWelcomeCalled;
		int printBoardStatusCount;
		boolean printEndStatusCalled;

		public TestGame(Board board, Player playerOne, Player playerTwo) {
			super(board, playerOne, playerTwo);
		}

		@Override
		public void printWelcome() {
			this.printWelcomeCalled = true;
		}

		@Override
		public void printBoardStatus() {
			this.printBoardStatusCount++;
		}

		@Override
		public void printCurrentTurn(Player currentPlayer) {
			this.lastPlayerPrinted = currentPlayer;
		}

		@Override
		public void printEndStatus() {
			this.printEndStatusCalled = true;
		}
	}

	private static class TestBoard implements Board {
		int turnsToRun;
		private List<Integer> markedPositionsInOrder = new ArrayList<>();

		@Override
		public char[] getStatus() {
			return null;
		}

		@Override
		public void mark(int position, char playerMark) {
			markedPositionsInOrder.add(position);
		}

		@Override
		public boolean hasWinner() {
			return false;
		}

		@Override
		public char getWinner() {
			return 0;
		}

		@Override
		public boolean hasEnded() {
			if (turnsToRun == 0) {
				return true;
			} else {
				turnsToRun--;
				return false;
			}
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isValid(int position) {
			return false;
		}

		@Override
		public boolean isMarked(int i) {
			return false;
		}

		@Override
		public Board copy() {
			return null;
		}

		@Override
		public List<Integer> possibleMoves() {
			return null;
		}
	}

	public static class TestPlayer implements Player {
		int makeMoveCount;
		Queue<Integer> movesQueue;
		private char marker;
		private Board board;

		public TestPlayer(char marker, Board board) {
			this.movesQueue = new ArrayDeque<>();
			this.marker = marker;
			this.board = board;
		}

		public void enqueueMoves(int... moves) {
			for (int m : moves)
				movesQueue.add(m);
		}

		@Override
		public char getMarker() {
			return marker;
		}

		@Override
		public void makeMove() {
			makeMoveCount++;
			board.mark(movesQueue.remove(), marker);
		}
	}
}
