package Frontend.UI;

import Backend.Tiles.Units.Playable.Action;
import Backend.Tiles.GameBoard;
import Backend.Patterns.Observer;

public interface IUserInterface extends Observer {
    void drawBoard(GameBoard gameBoard);
    void displayMessage(String message);
    Action getInput();
    int getIntegerChoice();
}
