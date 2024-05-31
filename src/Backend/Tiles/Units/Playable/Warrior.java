package Backend.Tiles.Units.Playable;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Enemies.Enemy;

import java.util.LinkedList;
import java.util.Random;

public class Warrior extends Player {
    private final int ABILITY_COOLDOWN;
    private final int INITIAL_COOLDOWN = 0;
    private int cooldown = 0;

    public Warrior(String name, int healthCapacity, int attack, int defense, int abilityCooldown) {
        super(name, healthCapacity, attack, defense);
        cooldown = INITIAL_COOLDOWN;
        ABILITY_COOLDOWN = abilityCooldown;
    }

    @Override
    public String description() {
        return String.format("Warrior: %s,\tAbility cooldown: %d", super.description(), cooldown);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        cooldown = 0;
        healthCapacity += 5 * level;
        attack += 2 * level;
        defense += level;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        // Avenger’s Shield
        send(name + " cast Avenger's shield!");
        if (cooldown > 0) {
            send("Casting failed! Ability is on cooldown!");
            return false;
        }
        cooldown = ABILITY_COOLDOWN;
        healthCurr = Math.min(healthCurr + 10 * defense, healthCapacity);
        LinkedList<Enemy> l = new LinkedList<>();
        board.getEnemies().stream().filter(enemy -> range(enemy) < 3).forEachOrdered(l::addFirst);
        if (l.size() > 0)
            fixedAttack(this, l.get(new Random().nextInt(l.size())), (int) (.1 * healthCapacity));
        return true;
    }

    @Override
    public void tick(GameBoard board) {
        if (cooldown > 0)
            cooldown--;
        super.tick(board);
    }
}
