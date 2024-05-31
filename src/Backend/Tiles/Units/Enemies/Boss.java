package Backend.Tiles.Units.Enemies;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.HeroicUnit;
import Backend.Tiles.Units.Playable.Player;

public class Boss extends Monster implements HeroicUnit {
    private final int ABILITY_FREQUENCY;

    private int combatTicks;

    public Boss(char tile, String name, int healthCapacity, int attack, int defense, int experienceValue, int visionRange, int abilityFrequency) {
        super(tile, name, healthCapacity, attack, defense, experienceValue, visionRange);
        combatTicks = 0;
        this.ABILITY_FREQUENCY = abilityFrequency;
    }

    @Override
    public void tick(GameBoard board) {
        Player player = board.getPlayer();
        if (range(player) < VISION_RANGE) {
            if (combatTicks == ABILITY_FREQUENCY) {
                combatTicks = 0;
                castAbility(board);
            } else {
                int dx = this.getPosition().getX() - player.getPosition().getX();
                int dy = this.getPosition().getY() - player.getPosition().getY();
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0)
                        moveUp(board);
                    else
                        moveDown(board);
                } else {
                    if (dy > 0)
                        moveLeft(board);
                    else
                        moveRight(board);
                }
            }
        } else
            moveRandom(board);

        // This is a bit unintuitive but the x-axis is vertical downwards and the y-axis is horizontal rightwards
    }

    @Override
    public boolean castAbility(GameBoard board) {
        // Shoot
        send("%s shoots %s!".formatted(name, board.getPlayer().getName()));
        return fixedAttack(this, board.getPlayer(), attack);
    }
}
