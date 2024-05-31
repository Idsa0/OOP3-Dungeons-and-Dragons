package Backend.Patterns;

import Backend.Tiles.Units.Playable.Player;

import java.util.ArrayList;

public interface PlayerDeathCallback {
    ArrayList<PlayerDeathObserver> playerDeathObservers = new ArrayList<>();

    void call(Player player);

    static void subscribe(PlayerDeathObserver o) {
        playerDeathObservers.add(o);
    }

    static void unSubscribe(PlayerDeathObserver o) {
        playerDeathObservers.remove(o);
    }
}