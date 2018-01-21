package console;

import application.PlayerFactory;
import players.DefaultPlayerFactory;

public class ConsoleMain {
    public static void main(String[] args) {
        PlayerFactory playerFactory = new DefaultPlayerFactory(System.in, System.out);
        new ConsoleTicTacToeApplication(System.in, System.out, playerFactory).run();
    }
}
