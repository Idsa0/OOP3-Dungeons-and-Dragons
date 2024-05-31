package Backend.Tiles.Units.Enemies;

import Backend.Tiles.Floor;
import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Playable.Player;

public class Trap extends Enemy {
    private final int INITIAL_TICKS_COUNT = 0;
    private final boolean INITIAL_VISIBILITY = true;
    private final int VISIBILITY_TIME;
    private final int INVISIBILITY_TIME;

    private int ticksCount;
    private boolean visible;

    public Trap(char tile, String name, int healthCapacity, int attack,
                int defense, int experienceValue, int visibilityTime, int invisibilityTime) {
        super(tile, name, healthCapacity, attack, defense, experienceValue);
        this.VISIBILITY_TIME = visibilityTime;
        this.INVISIBILITY_TIME = invisibilityTime;
        ticksCount = INITIAL_TICKS_COUNT;
        visible = INITIAL_VISIBILITY;
    }

    @Override
    public void tick(GameBoard board) {
        visible = ticksCount < VISIBILITY_TIME;
        if (ticksCount == (VISIBILITY_TIME + INVISIBILITY_TIME))
            ticksCount = 0;
        else
            ticksCount++;
        Player player = board.getPlayer();
        if (range(player) < 2)
            meleeAttack(this, player);
    }

    @Override
    public String toString() {
        if (visible)
            return String.valueOf(tile);
        return new Floor().toString();
    }

    @Override
    protected void die() {
        send(name + " was destroyed!");
        call(this);
    }
}
