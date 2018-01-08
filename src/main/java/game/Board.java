package game;

public interface Board {
	char[] getStatus();

	void mark(int position, char playerMark);

	boolean hasWinner();

	char getWinner();

	boolean hasEnded();

	int size();

	boolean isValid(int position);

	boolean isMarked(int i);

	Board copy();
}