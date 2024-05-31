package Backend.Patterns;

import Backend.Tiles.Units.Playable.Player;

public interface PlayerDeathObserver {
    void onPlayerDeath(Player player);
}
