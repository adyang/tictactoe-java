package application;

public enum PlayerNumber {
    ONE ("one", 'X', 'O'),
    TWO ("two", 'O', 'X');

    public final String text;
    public final char marker;
    public final char opponentMarker;

    PlayerNumber(String text, char marker, char opponentMarker) {
        this.text = text;
        this.marker = marker;
        this.opponentMarker = opponentMarker;
    }
}
