package gui;

import java.util.ArrayList;
import java.util.List;

public class DisplayBoard {
    public List<DisplayCell> cells = new ArrayList<>();

    public static class DisplayCell {
        public final String marker;
        public final int xPos;
        public final int yPos;
        public Runnable actionHandler;

        public DisplayCell(String marker, int xPos, int yPos) {
            this.marker = marker;
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }
}
