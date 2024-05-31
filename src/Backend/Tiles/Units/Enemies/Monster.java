package Backend.Tiles.Units.Enemies;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Playable.Player;

public class Monster extends Enemy {
    final int VISION_RANGE;

    public Monster(char tile, String name, int healthCapacity, int attack,
                   int defense, int experienceValue, int visionRange) {
        super(tile, name, healthCapacity, attack, defense, experienceValue);
        this.VISION_RANGE = visionRange;
    }

    @Override
    public void tick(GameBoard board) {
        Player player = board.getPlayer();
        if (range(player) < VISION_RANGE) {
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
        } else
            moveRandom(board);

        // This is a bit unintuitive but the x-axis is vertical downwards and the y-axis is horizontal rightwards
    }

    @Override
    public String description() {
        return String.format("%s,\tVision Range: %s", super.description(), VISION_RANGE);
    }
}
