package Backend.Tiles.Units.Playable;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Enemies.Enemy;

import java.util.LinkedList;

public class Rogue extends Player {
    private final int ENERGY_MAX = 100;
    private final int COST;

    private int energyCurr;

    public Rogue(String name, int healthCapacity, int attack, int defense, int cost) {
        super(name, healthCapacity, attack, defense);
        this.energyCurr = ENERGY_MAX;
        this.COST = cost;
    }

    @Override
    public String description() {
        return String.format("Rogue: %s,\tEnergy: %d/%d", super.description(), energyCurr, ENERGY_MAX);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        energyCurr = ENERGY_MAX;
        attack += 3 * level;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        //  Fan of Knives
        send(name + " cast Fan of Knives!");
        if (energyCurr < COST) {
            send("Casting failed! Not enough energy!");
            return false;
        }
        energyCurr -= COST;
        LinkedList<Enemy> l = new LinkedList<>();
        board.getEnemies().stream().filter(enemy -> range(enemy) < 2).forEachOrdered(l::addFirst);
        l.forEach(enemy -> fixedAttack(this, enemy, attack));
        return true;
    }

    @Override
    public void tick(GameBoard board) {
        energyCurr = Math.min(energyCurr + 10, ENERGY_MAX);
        super.tick(board);
    }
}
