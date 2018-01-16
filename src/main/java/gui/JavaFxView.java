package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static gui.DisplayBoard.*;

public class JavaFxView implements View {
    private final Scene scene;

    public JavaFxView(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void displayBoard(DisplayBoard displayBoard) {
        GridPane board = new GridPane();
        board.setId("board");
        for (DisplayCell displayCell : displayBoard.cells) {
            Label cell = new Label(displayCell.marker);
            cell.setId("cell-" + indexPos(displayCell));
            board.add(cell, displayCell.xPos, displayCell.yPos);
        }
        scene.setRoot(board);
    }

    private int indexPos(DisplayCell displayCell) {
        return displayCell.xPos + displayCell.yPos * 3;
    }

    @Override
    public void displayCurrentMarker(String marker) {

    }

    @Override
    public void displayWinner(String winner) {

    }

    @Override
    public void displayDraw() {

    }
}
