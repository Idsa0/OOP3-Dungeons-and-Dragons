package Backend.Tiles.Units;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Tile;

import java.util.Random;

public abstract class Unit extends Tile {
    protected String name;
    protected int healthCapacity;
    protected int healthCurr;
    protected int attack;
    protected int defense;

    public Unit(char tile, String name, int healthCapacity, int attack, int defense) {
        super(tile);
        this.name = name;
        this.healthCapacity = healthCapacity;
        this.healthCurr = this.healthCapacity;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public int getHealthCurr() {
        return healthCurr;
    }

    public int getHealthCapacity() {
        return healthCapacity;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    /**
     * @return True if unit died, false otherwise.
     */
    public boolean takeDamage(int amount) {
        if (amount <= 0)
            return false;
        healthCurr = Math.max(healthCurr - amount, 0);
        send("%s took %d damage! (%d/%d)".formatted(name, amount, healthCurr, healthCapacity));
        if (healthCurr > 0)
            return false;
        die();
        return true;
    }

    public int rollAttack() {
        return new Random().nextInt(attack + 1);
    }

    public int rollDefence() {
        return new Random().nextInt(defense + 1);
    }

    public abstract String description();

    public boolean moveUp(GameBoard board) {
        return board.getBoard()[getPosition().getX() - 1][getPosition().getY()].accept(this, board);
    }

    public boolean moveDown(GameBoard board) {
        return board.getBoard()[getPosition().getX() + 1][getPosition().getY()].accept(this, board);
    }

    public boolean moveLeft(GameBoard board) {
        return board.getBoard()[getPosition().getX()][getPosition().getY() - 1].accept(this, board);
    }

    public boolean moveRight(GameBoard board) {
        return board.getBoard()[getPosition().getX()][getPosition().getY() + 1].accept(this, board);
    }

    protected boolean moveRandom(GameBoard board) {
        return switch (new Random().nextInt(5)) {
            case 0 -> moveUp(board);
            case 1 -> moveDown(board);
            case 2 -> moveLeft(board);
            case 3 -> moveRight(board);
            default -> false;
        };
    }

    protected abstract void die();

    /**
     * The attacker and the defender roll attack and defence respectively.
     * @return True if the defender was killed, false otherwise.
     */
    public boolean meleeAttack(Unit attacker, Unit defender) {
        send("%s attacks %s!".formatted(attacker.name, defender.name));
        send(attacker.description());
        send(defender.description());
        int att = attacker.rollAttack();
        send("%s rolled %d to attack!".formatted(attacker.name, att));
        int def = defender.rollDefence();
        send("%s rolled %d to defend!".formatted(defender.name, def));
        return defender.takeDamage(att - def);
    }

    /**
     * The attacker does a specific amount of damage, the defender rolls defence.
     * @param damage The damage done by the attacker.
     * @return True if the defender was killed, false otherwise.
     */
    public boolean fixedAttack(Unit attacker, Unit defender, int damage) {
        send("%s attacks %s!".formatted(attacker.name, defender.name));
        int def = defender.rollDefence();
        send("%s rolled %d to defend!".formatted(defender.name, def));
        return defender.takeDamage(damage - def);
    }
}
