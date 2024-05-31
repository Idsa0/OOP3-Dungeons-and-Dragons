package Frontend.UI;

import Backend.Tiles.Units.Playable.Action;
import Backend.Tiles.GameBoard;
import Backend.Tiles.Tile;

import java.util.Scanner;

public class CLI implements IUserInterface {
    private final Scanner input = new Scanner(System.in);

    @Override
    public void drawBoard(GameBoard gameBoard) {
        Tile[][] board = gameBoard.getBoard();
        for (Tile[] tiles : board) {
            for (Tile tile : tiles)
                System.out.print(tile);
            System.out.println();
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public Action getInput() {
        return switch (input.nextLine().toLowerCase()) {
            case "w" -> Action.MOVE_UP;
            case "s" -> Action.MOVE_DOWN;
            case "a" -> Action.MOVE_LEFT;
            case "d" -> Action.MOVE_RIGHT;
            case "e" -> Action.SPECIAL;
            case "q" -> Action.DO_NOTHING;
            case "!impulse101" -> Action.DEBUG_ULTIMATE_STRENGTH;
            case "!godmode" -> Action.DEBUG_ULTIMATE_HEALTH;
            default -> Action.ILLEGAL_ACTION;
        };
    }

    @Override
    public int getIntegerChoice() {
        String s = input.nextLine();
        while (!isInteger(s))
            s = input.nextLine();
        return Integer.parseInt(s);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onEvent(String message) {
        displayMessage(message);
    }
}
