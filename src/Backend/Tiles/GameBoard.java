package Backend.Tiles;

import Backend.Patterns.EnemyDeathCallback;
import Backend.Patterns.EnemyDeathObserver;
import Backend.Patterns.PlayerDeathCallback;
import Backend.Patterns.PlayerDeathObserver;
import Backend.Tiles.Units.Enemies.Boss;
import Backend.Tiles.Units.Enemies.Enemy;
import Backend.Tiles.Units.Enemies.Monster;
import Backend.Tiles.Units.Enemies.Trap;
import Backend.Tiles.Units.Playable.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameBoard implements EnemyDeathObserver, PlayerDeathObserver {
    private TileFactory tf;
    private Tile[][] board;
    private LinkedList<Enemy> enemies;
    private Player player;
    private boolean done;
    private boolean playerAlive;

    public GameBoard() {
        enemies = new LinkedList<>();
        tf = new TileFactory();
        done = false;
        playerAlive = true;
        EnemyDeathCallback.subscribe(this);
        PlayerDeathCallback.subscribe(this);
    }

    public Tile[][] getBoard() {
        return board;
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public TileFactory getTF() {
        return tf;
    }

    public void tick(Action input) {
        player.takeAction(input, this);
        player.tick(this);
        enemies.forEach(enemy -> enemy.tick(this));
    }

    public void createPlayer(int i) {
        this.player = tf.producePlayer(i);
        tf.selected = player;
        EnemyDeathCallback.subscribe(player);
    }

    public void setBoard(char[][] board) {
        this.board = tf.makeTileMatrix(board, enemies);
    }

    @Override
    public void onEnemyDeath(Enemy enemy) {
        Position position = new Position(enemy.getPosition());
        board[position.getX()][position.getY()] = tf.produceFloor();
        board[position.getX()][position.getY()].setPosition(position);
        enemies.remove(enemy);
        done = enemies.size() == 0;
    }

    @Override
    public void onPlayerDeath(Player player) {
        playerAlive = false;
    }

    public boolean isPlayerAlive() {
        return playerAlive;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public class TileFactory {
        private final List<Supplier<Player>> playersList;
        private final Map<Character, Supplier<Enemy>> enemiesMap;
        private Player selected;

        public TileFactory() {
            playersList = initPlayers();
            enemiesMap = initEnemies();
        }

        private Map<Character, Supplier<Enemy>> initEnemies() {
            List<Supplier<Enemy>> enemies = Arrays.asList(
                    () -> new Monster('s', "Lannister Solider", 80, 8, 3, 25, 3),
                    () -> new Monster('k', "Lannister Knight", 200, 14, 8, 50, 4),
                    () -> new Monster('q', "Queen's Guard", 400, 20, 15, 100, 5),
                    () -> new Monster('z', "Wright", 600, 30, 15, 100, 3),
                    () -> new Monster('b', "Bear-Wright", 1000, 75, 30, 250, 4),
                    () -> new Monster('g', "Giant-Wright", 1500, 100, 40, 500, 5),
                    () -> new Monster('w', "White Walker", 2000, 150, 50, 1000, 6),
                    () -> new Boss('M', "The Mountain", 1000, 60, 25,  500, 6, 5),
                    () -> new Boss('C', "Queen Cersei", 100, 10, 10,1000, 1, 8),
                    () -> new Boss('K', "Night's King", 5000, 300, 150, 5000, 8, 3),
                    () -> new Trap('B', "Bonus Trap", 1, 1, 1, 250, 1, 10),
                    () -> new Trap('Q', "Queen's Trap", 250, 50, 10, 100, 3, 10),
                    () -> new Trap('D', "Death Trap", 500, 100, 20, 250, 1, 10)
            );

            return enemies.stream().collect(Collectors.toMap(s -> s.get().getTile(), Function.identity()));
        }

        private List<Supplier<Player>> initPlayers() {
            return Arrays.asList(
                    () -> new Warrior("Jon Snow", 300, 30, 4, 3),
                    () -> new Warrior("The Hound", 400, 20, 6, 5),
                    () -> new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
                    () -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
                    () -> new Rogue("Arya Stark", 150, 40, 2, 20),
                    () -> new Rogue("Bronn", 250, 35, 3, 50),
                    () -> new Hunter("Ygritte", 220, 30, 2, 6),
                    () -> new Mage("Lord Idan of House Saltzman", 1000, 100, 100, 1000, 100, 10, 10, 10),
                    () -> new Warrior("Hodor", 1000, 1, 100, 100)
            );
        }

        public List<Player> listPlayers() {
            return playersList.stream().map(Supplier::get).collect(Collectors.toList());
        }

        public Enemy produceEnemy(char tile) {
            return enemiesMap.get(tile).get();
        }

        public Player producePlayer(int i) {
            return listPlayers().get(i);
        }

        public Floor produceFloor() {
            return new Floor();
        }

        public Wall produceWall() {
            return new Wall();
        }

        public Tile[][] makeTileMatrix(char[][] cMatrix, List<Enemy> enemies) {
            Tile[][] tMatrix = new Tile[cMatrix.length][cMatrix[0].length];
            for (int i = 0; i < cMatrix.length; i++)
                for (int j = 0; j < cMatrix[i].length; j++) {
                    switch (cMatrix[i][j]) {
                        case '.' -> tMatrix[i][j] = produceFloor();
                        case '#' -> tMatrix[i][j] = produceWall();
                        case '@' -> tMatrix[i][j] = selected;
                        default -> {
                            if (enemiesMap.containsKey(cMatrix[i][j])) {
                                Enemy e = produceEnemy(cMatrix[i][j]);
                                tMatrix[i][j] = e;
                                enemies.add(e);
                                break;
                            }
                            throw new RuntimeException("Read an unrecognizable char, make sure the level file is functional.");
                        }
                    }
                    tMatrix[i][j].setPosition(new Position(i, j));
                }
            return tMatrix;
        }
    }
}
