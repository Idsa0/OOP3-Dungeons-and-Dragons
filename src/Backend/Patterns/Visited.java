package Backend.Patterns;

import Backend.Tiles.GameBoard;

public interface Visited {
    boolean accept(Visitor v, GameBoard board);
}
