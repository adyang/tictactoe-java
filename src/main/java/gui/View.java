package gui;

import application.PlayerNumber;

public interface View {
    void displayBoard(DisplayBoard displayBoard);

    void displayCurrentMarker(String marker);

    void displayWinner(String winner);

    void displayDraw();

    void displayWelcome();

    void displayGameConfig(DisplayGameConfig displayGameConfig);

    boolean validateGameConfig();

    String getPlayerTypeFor(PlayerNumber playerNumber);

    void displayPlayAgain(Runnable playAgainHandler);
}
