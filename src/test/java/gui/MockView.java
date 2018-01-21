package gui;

import application.PlayerNumber;

class MockView implements View {
    DisplayBoard displayBoard;
    String displayedMarker;
    String displayedWinner;
    boolean displayDrawCalled;
    boolean displayWelcomeCalled;
    DisplayGameConfig displayGameConfig;
    String playerOneSelectedType;
    String playerTwoSelectedType;
    boolean isValidGameConfig;

    @Override
    public void displayBoard(DisplayBoard displayBoard) {
        this.displayBoard = displayBoard;
    }

    @Override
    public void displayCurrentMarker(String marker) {
        this.displayedMarker = marker;
    }

    @Override
    public void displayWinner(String winner) {
        this.displayedWinner = winner;
    }

    @Override
    public void displayDraw() {
        this.displayDrawCalled = true;
    }

    @Override
    public void displayWelcome() {
        displayWelcomeCalled = true;
    }

    @Override
    public void displayGameConfig(DisplayGameConfig displayGameConfig) {
        this.displayGameConfig = displayGameConfig;
    }

    @Override
    public boolean validateGameConfig() {
        return this.isValidGameConfig;
    }

    @Override
    public String getPlayerTypeFor(PlayerNumber playerNumber) {
        switch (playerNumber) {
            case ONE:
                return playerOneSelectedType;
            case TWO:
                return playerTwoSelectedType;
        }
        throw new IllegalArgumentException("Unhandled PlayerNumber: " + playerNumber);
    }
}
