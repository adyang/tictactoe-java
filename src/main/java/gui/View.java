package gui;

public interface View {
    void displayBoard(DisplayBoard displayBoard);

    void displayCurrentMarker(String marker);

    void displayWinner(String winner);

    void displayDraw();
}
