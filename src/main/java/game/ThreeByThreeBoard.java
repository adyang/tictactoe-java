package game;

import java.util.HashSet;
import java.util.Set;

public class ThreeByThreeBoard implements Board {
	private static final int BOARD_SIZE = 3;
	private char[] cells;
	private char winner;
	private Set<int[]> winningPaths;

	public ThreeByThreeBoard() {
		this.cells = new char[9];
		generateWinningPaths();
	}

	private void generateWinningPaths() {
		winningPaths = new HashSet<>();
		winningPaths.addAll(generateHorizontalWinningPaths());
		winningPaths.addAll(generateVerticalWinningPaths());
		winningPaths.addAll(generateDiagonalWinningPaths());
	}

	private Set<int[]> generateHorizontalWinningPaths() {
		Set<int[]> paths = new HashSet<>();
		for (int rowStep = 0; rowStep < BOARD_SIZE; rowStep++) {
			int[] winningPath = new int[BOARD_SIZE];
			for (int cellIdx = 0; cellIdx < BOARD_SIZE; cellIdx++) {
				winningPath[cellIdx] = cellIdx + rowStep * BOARD_SIZE;
			}
			paths.add(winningPath);
		}
		return paths;
	}

	private Set<int[]> generateVerticalWinningPaths() {
		Set<int[]> paths = new HashSet<>();
		for (int colStep = 0; colStep < BOARD_SIZE; colStep++) {
			int[] winningPath = new int[BOARD_SIZE];
			for (int cellIdx = 0; cellIdx < BOARD_SIZE; cellIdx++) {
				winningPath[cellIdx] = cellIdx * BOARD_SIZE + colStep;
			}
			paths.add(winningPath);
		}
		return paths;
	}

	private Set<int[]> generateDiagonalWinningPaths() {
		Set<int[]> paths = new HashSet<>();
		addFirstDiagonalWinningPath(paths);
		addSecondDiagonalWinningPath(paths);
		return paths;
	}

	private void addFirstDiagonalWinningPath(Set<int[]> paths) {
		int[] winningPath = new int[BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			winningPath[i] = i + i * BOARD_SIZE;
		}
		paths.add(winningPath);
	}

	private void addSecondDiagonalWinningPath(Set<int[]> paths) {
		int[] winningPath = new int[BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			winningPath[i] = BOARD_SIZE - 1 - i + i * BOARD_SIZE;
		}
		paths.add(winningPath);
	}

	@Override
	public char[] getStatus() {
		return cells.clone();
	}

	@Override
	public boolean isValid(int position) {
		return !isOutOfRange(position) && !isMarked(position);
	}

	@Override
	public void mark(int position, char playerMark) {
		validate(position);
		cells[position] = playerMark;
		winner = determineWinnerIfPresent();
	}

	private void validate(int position) {
		if (isOutOfRange(position))
			throw new IllegalArgumentException("Invalid position: " + position);
		if (isMarked(position))
			throw new IllegalArgumentException("Position " + position + " is already marked.");
	}

	private boolean isOutOfRange(int position) {
		return position < 0 || position >= BOARD_SIZE * BOARD_SIZE;
	}

	private char determineWinnerIfPresent() {
		for (int[] winningPath : winningPaths) {
			if (hasWinnerOn(winningPath)) {
				return cells[winningPath[0]];
			}
		}
		return 0;
	}

	@Override
	public boolean hasWinner() {
		return winner != 0;
	}

	private boolean hasWinnerOn(int[] winningPath) {
		for (int i = 1; i < winningPath.length; i++) {
			if (!isMarked(winningPath[i]) || !isSameMark(winningPath[i - 1], winningPath[i]))
				return false;
		}
		return true;
	}

	private boolean isMarked(int position) {
		return cells[position] != 0;
	}

	private boolean isSameMark(int positionOne, int positionTwo) {
		return cells[positionOne] == cells[positionTwo];
	}

	@Override
	public char getWinner() {
		return winner;
	}

	@Override
	public boolean hasEnded() {
		return hasWinner() || isAllMarked();
	}

	private boolean isAllMarked() {
		for (char cell : cells)
			if (cell == 0)
				return false;
		return true;
	}

	@Override
	public int size() {
		return BOARD_SIZE;
	}
}
