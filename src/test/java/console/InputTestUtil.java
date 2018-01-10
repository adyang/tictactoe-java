package console;

public class InputTestUtil {
    public static String toInputString(String... inputs) {
        return String.join("%n", inputs) + "%n";
    }
}
