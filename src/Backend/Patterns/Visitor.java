package Backend.Patterns;

import Backend.Tiles.*;
import Backend.Tiles.Units.Enemies.Enemy;
import Backend.Tiles.Units.Playable.Player;

public interface Visitor {
    boolean visit(Floor floor, GameBoard board);

    boolean visit(Wall wall, GameBoard board);

    boolean visit(Player player, GameBoard board);

    boolean visit(Enemy enemy, GameBoard board);
}
