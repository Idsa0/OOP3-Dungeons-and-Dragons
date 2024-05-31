package Backend.Patterns;

import Backend.Tiles.Units.Enemies.Enemy;

public interface EnemyDeathObserver {
    void onEnemyDeath(Enemy enemy);
}
