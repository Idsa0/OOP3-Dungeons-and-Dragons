package Backend.Tiles.Units.Playable;

import Backend.Patterns.EnemyDeathObserver;
import Backend.Patterns.PlayerDeathCallback;
import Backend.Tiles.Floor;
import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Enemies.Enemy;
import Backend.Tiles.Units.HeroicUnit;
import Backend.Tiles.Units.Unit;
import Backend.Patterns.Visitor;

public abstract class Player extends Unit implements EnemyDeathObserver, PlayerDeathCallback, HeroicUnit {
    private final int DEFAULT_STARTING_LEVEL = 1;
    private final int DEFAULT_STARTING_EXPERIENCE = 0;

    protected int experience;
    protected int level;

    public Player(String name, int healthCapacity, int attack, int defense) {
        super('@', name, healthCapacity, attack, defense);
        experience = DEFAULT_STARTING_EXPERIENCE;
        level = DEFAULT_STARTING_LEVEL;
        healthCurr = healthCapacity;
    }

    @Override
    public String description() {
        return String.format("%s,\tLevel: %d,\t Experience: %d/%d\tHealth: %d/%d,\tAttack: %d,\tDefense: %d", getName(), level, experience, 50 * level, getHealthCurr(), getHealthCapacity(), getAttack(), getDefense());
    }

    public void levelUp() {
        experience -= 50 * level;
        level++;
        send("Level up! %s Reached level %d!".formatted(name, level));
        healthCapacity += 10 * level;
        healthCurr = healthCapacity;
        attack += 4 * level;
        defense += level;
    }

    @Override
    public void tick(GameBoard board) {
        // Do nothing
    }

    @Override
    public boolean visit(Floor floor, GameBoard board) {
        return swap(this, floor, board);
    }

    @Override
    public boolean visit(Enemy enemy, GameBoard board) {
        if (meleeAttack(this, enemy))
            swap(this, board.getBoard()[enemy.getPosition().getX()][enemy.getPosition().getY()], board);
        return true;
    }

    @Override
    public boolean accept(Visitor v, GameBoard board) {
        return v.visit(this, board);
    }

    public boolean takeAction(Action input, GameBoard board) {
        return switch (input) {
            case MOVE_UP -> moveUp(board);
            case MOVE_DOWN -> moveDown(board);
            case MOVE_LEFT -> moveLeft(board);
            case MOVE_RIGHT -> moveRight(board);
            case SPECIAL -> castAbility(board);
            case DEBUG_ULTIMATE_STRENGTH -> {
                attack = 1000000;
                yield true;
            }
            case DEBUG_ULTIMATE_HEALTH -> {
                healthCapacity = 1000000;
                healthCurr = healthCapacity;
                yield true;
            }
            default -> true; // Do nothing
        };
    }

    @Override
    protected void die() {
        tile = 'X';
        call(this);
    }

    public void gainExperience(int exp) {
        experience += exp;
        if (experience >= 50 * level)
            levelUp();
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public void onEnemyDeath(Enemy enemy) {
        gainExperience(enemy.getExperienceValue());
    }

    @Override
    public void call(Player player) {
        playerDeathObservers.forEach(o -> o.onPlayerDeath(this));
    }


}
