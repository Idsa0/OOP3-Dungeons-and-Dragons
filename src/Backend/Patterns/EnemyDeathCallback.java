package Backend.Patterns;

import Backend.Tiles.Units.Enemies.Enemy;

import java.util.ArrayList;

public interface EnemyDeathCallback {
    ArrayList<EnemyDeathObserver> enemyDeathObservers = new ArrayList<>();

    void call(Enemy enemy);

    static void subscribe(EnemyDeathObserver o) {
        enemyDeathObservers.add(o);
    }

    static void unSubscribe(EnemyDeathObserver o) {
        enemyDeathObservers.remove(o);
    }
}