package Backend.Tiles.Units.Enemies;

import Backend.Patterns.EnemyDeathCallback;
import Backend.Tiles.Floor;
import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Playable.Player;
import Backend.Tiles.Units.Unit;
import Backend.Patterns.Visitor;

public abstract class Enemy extends Unit implements EnemyDeathCallback {
    private final int experienceValue;

    public Enemy(char tile, String name, int healthCapacity, int attack, int defense, int experienceValue) {
        super(tile, name, healthCapacity, attack, defense);
        this.experienceValue = experienceValue;
    }

    @Override
    public boolean visit(Floor floor, GameBoard board) {
        return swap(this, floor, board);
    }

    @Override
    public boolean visit(Player player, GameBoard board) {
        if (!meleeAttack(this, player))
            return false;
        swap(this, player, board);
        return true;
    }

    @Override
    public boolean accept(Visitor v, GameBoard board) {
        return v.visit(this, board);
    }

    @Override
    protected void die() {
        send(name + " died!");
        call(this);
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public void call(Enemy enemy) {
        enemyDeathObservers.forEach(o -> o.onEnemyDeath(this));
    }

    @Override
    public String description() {
        return String.format("%s,\tHealth: %d/%d,\tAttack: %s,\tDefense: %s", getName(), getHealthCurr(), getHealthCapacity(), getAttack(), getDefense());
    }
}
