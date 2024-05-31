package Backend.Tiles;

import Backend.Patterns.MessageCallback;
import Backend.Tiles.Units.Enemies.Enemy;
import Backend.Tiles.Units.Playable.Player;
import Backend.Patterns.Visited;
import Backend.Patterns.Visitor;

public abstract class Tile implements Visitor, Visited, MessageCallback {
    protected char tile;
    private Position position;

    public Tile(char tile) {
        this.tile = tile;
    }

    public char getTile() {
        return tile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double range(Tile tile) {
        return this.position.distance(tile.position);
    }

    public abstract void tick(GameBoard board);

    @Override
    public String toString() {
        return String.valueOf(tile);
    }

    public boolean swap(Tile t1, Tile t2, GameBoard board) {
        Position p1 = new Position(t1.position);
        board.getBoard()[t1.position.getX()][t1.position.getY()] = t2;
        board.getBoard()[t2.position.getX()][t2.position.getY()] = t1;
        t1.position = new Position(t2.position);
        t2.position = new Position(p1);
        return true;
    }

    @Override
    public boolean visit(Wall wall, GameBoard board) {
        return false;
    }

    @Override
    public boolean visit(Enemy enemy, GameBoard board) {
        return false;
    }

    @Override
    public boolean visit(Floor floor, GameBoard board) {
        return false;
    }

    @Override
    public boolean visit(Player player, GameBoard board) {
        return false;
    }

    @Override
    public void send(String message) {
        messageObservers.forEach(o -> o.onEvent(message));
    }
}
