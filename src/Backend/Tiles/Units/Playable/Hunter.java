package Backend.Tiles.Units.Playable;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Enemies.Enemy;

import java.util.LinkedList;

public class Hunter extends Player {
    private final int INITIAL_ARROWS_COUNT = 10 * level;

    private int range;
    private int arrowsCount;
    private int ticksCount;

    public Hunter(String name, int healthCapacity, int attack, int defense, int range) {
        super(name, healthCapacity, attack, defense);
        this.range = range;
        this.arrowsCount = INITIAL_ARROWS_COUNT;
        this.ticksCount = 0;
    }

    @Override
    public String description() {
        return String.format("Hunter: %s,\tArrows: %d", super.description(), arrowsCount);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        arrowsCount += 10 * level;
        attack += 2 * level;
        defense += level;
    }

    @Override
    public void tick(GameBoard board) {
        if (ticksCount == 10) {
            arrowsCount += level;
            ticksCount = 0;
        } else
            ticksCount++;
        super.tick(board);
    }

    @Override
    public boolean castAbility(GameBoard board) {
        // Shoot
        send(name + " cast Shoot!");
        if (arrowsCount == 0) {
            send("Casting failed! Not enough arrows!");
            return false;
        }
        LinkedList<Enemy> l = new LinkedList<>();
        board.getEnemies().stream().filter(enemy -> range(enemy) < range).forEachOrdered(l::addFirst);
        if (l.size() == 0) {
            send("Casting failed! No enemies in range!");
            return false;
        }
        arrowsCount -= 1;
        Enemy closestEnemy = l.get(0);
        double closestRange = range(closestEnemy);
        for (Enemy enemy : l)
            if (range(enemy) < closestRange) {
                closestEnemy = enemy;
                closestRange = range(enemy);
            }
        fixedAttack(this, closestEnemy, attack);
        return true;
    }

    public int getArrowsCount() {
        return this.arrowsCount;
    }
}
