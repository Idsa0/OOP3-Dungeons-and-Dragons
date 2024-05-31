package Backend.Tiles;

import Backend.Patterns.Visitor;

public class Wall extends Tile {
    public Wall() {
        super('#');
    }

    @Override
    public void tick(GameBoard board) {
        // Do nothing
    }

    @Override
    public boolean accept(Visitor v, GameBoard board) {
        return v.visit(this, board);
    }
}
