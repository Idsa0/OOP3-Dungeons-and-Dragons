package Frontend;

import Backend.Tiles.Units.Playable.Action;
import Backend.Tiles.GameBoard;
import Backend.Patterns.MessageCallback;
import Backend.Tiles.Units.Playable.Player;
import Frontend.UI.CLI;
import Frontend.UI.IUserInterface;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {
    private static final IUserInterface ui = new CLI();

    public static void main(String[] args) {

        // Check if arguments are given
        if (args.length == 0) {
            ui.displayMessage("Pass the path to the levels directory in order to play");
            ui.getInput();
            System.exit(-1);
        }

        // Load all levels into memory
        List<char[][]> levels = getLevels(args[0]);
        int currentLevel = 0;

        // Setup GameBoard
        MessageCallback.subscribe(ui);
        GameBoard game = new GameBoard();

        // Show playable characters
        ui.displayMessage("Select a character:");
        List<Player> players = game.getTF().listPlayers();
        IntStream.range(0, players.size()).mapToObj(i -> (i + 1) + ". " + players.get(i).description()).forEachOrdered(ui::displayMessage);

        // Get player choice
        while (true) {
            int choice = ui.getIntegerChoice();
            if (choice >= 1 && choice <= players.size()) {
                game.createPlayer(choice - 1);
                break;
            }
        }

        // Load the first level
        game.setBoard(levels.get(currentLevel));

        // Run
        Action input;
        ui.drawBoard(game);
        ui.displayMessage(game.getPlayer().description());
        while (!game.isDone()) {
            input = ui.getInput();
            if (input != Action.ILLEGAL_ACTION) {
                game.tick(input);
                ui.drawBoard(game);
                ui.displayMessage(game.getPlayer().description());
                if (!game.isPlayerAlive()) {
                    ui.displayMessage("GAME OVER!");
                    ui.getInput();
                    System.exit(0);
                }
                if (game.isDone()) {
                    currentLevel++;
                    if (currentLevel >= levels.size()) {
                        ui.displayMessage("YOU WIN!");
                        ui.getInput();
                        System.exit(0);
                    }
                    ui.displayMessage("Level complete!");
                    game.setBoard(levels.get(currentLevel));
                    ui.drawBoard(game);
                    ui.displayMessage(game.getPlayer().description());
                    game.setDone(false);
                }
            }
        }
    }

    public static List<char[][]> getLevels(String path) {
        try {
            File[] files = new File(path).listFiles();
            assert files != null;
            return Arrays.stream(files).map(file -> loadLevel(file.getPath())).collect(Collectors.toList());
        } catch (Exception e) {
            ui.displayMessage("Could not load levels");
            System.exit(1);
        }
        return null;
    }

    public static char[][] loadLevel(String path) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            char[][] level = new char[allLines.size()][];
            Arrays.setAll(level, i -> allLines.get(i).toCharArray());
            return level;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
