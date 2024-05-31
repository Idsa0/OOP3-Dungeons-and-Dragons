package Tests;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Position;
import Backend.Tiles.Units.Playable.*;
import org.junit.*;

public class TestPlayer {
    private static GameBoard createBoard(char[][] level) {
        GameBoard board = new GameBoard();
        board.createPlayer(1);
        board.setBoard(level);
        board.createPlayer(1);
        board.getPlayer().setPosition(new Position(3, 1));
        return board;
    }

    char[][] level =
            {
                    {'#', '#', '#', '#', '#', '#'},
                    {'#', '.', '.', '.', '.', '#'},
                    {'#', 'z', '.', '.', '.', '#'},
                    {'#', '@', '.', '.', '.', '#'},
                    {'#', '.', '.', '.', '.', '#'},
                    {'#', '#', '#', '#', '#', '#'}
            };
    GameBoard board = createBoard(level);
    Player p = board.getPlayer();
    Hunter h = new Hunter("The Hunter", 50, 20, 20, 1);
    Mage m = new Mage("Mage", 50, 20, 20, 30, 100, 25, 0, 2);
    Rogue r = new Rogue("rogue", 50, 20, 20, 15);
    Warrior w = new Warrior("The warrior", 50, 20, 20, 2);

    @Before
    public void setTests() {
    }

    @Test
    public void testLevelUp() {
        h.setExperience(100);
        m.setExperience(100);
        r.setExperience(100);
        w.setExperience(100);
        h.levelUp();
        m.levelUp();
        r.levelUp();
        w.levelUp();
        Assert.assertEquals(50, h.getExperience());
        Assert.assertEquals(2, h.getLevel());
        Assert.assertEquals(70, h.getHealthCapacity());
        Assert.assertEquals(28, m.getAttack());
        Assert.assertEquals(32, h.getAttack());
        Assert.assertEquals(24, h.getDefense());
        Assert.assertEquals(30, h.getArrowsCount());
        Assert.assertEquals(150, m.getManaPool());
        Assert.assertEquals(50, m.getSpellPower());
        Assert.assertEquals(34, r.getAttack());
        Assert.assertEquals(32, w.getAttack());
        Assert.assertEquals(24, w.getDefense());
        Assert.assertEquals(80, w.getHealthCapacity());
    }

    @Test
    public void testSpecialAbility() {
        p.castAbility(board);
        Assert.assertTrue(board.getEnemies().get(0).getHealthCurr() < 600);
    }

    @Test
    public void testPlayerMove() {
        Position pos = p.getPosition();
        p.takeAction(Action.MOVE_LEFT, board); // move into a wall
        Assert.assertEquals(pos, p.getPosition());
        Assert.assertEquals('#', board.getBoard()[3][0].getTile());
        p.takeAction(Action.MOVE_RIGHT, board); // move into a floor
        Assert.assertEquals(3, p.getPosition().getX());
        Assert.assertEquals(2, p.getPosition().getY());
        Assert.assertEquals('@', board.getBoard()[3][2].getTile());
        Assert.assertEquals('.', board.getBoard()[3][1].getTile());
        p.takeAction(Action.MOVE_UP, board); // move into a floor
        Assert.assertEquals(2, p.getPosition().getX());
        Assert.assertEquals(2, p.getPosition().getY());
        Assert.assertEquals('@', board.getBoard()[2][2].getTile());
        Assert.assertEquals('.', board.getBoard()[3][2].getTile());
        p.takeAction(Action.MOVE_DOWN, board); // move into a floor
        Assert.assertEquals(3, p.getPosition().getX());
        Assert.assertEquals(2, p.getPosition().getY());
        Assert.assertEquals('@', board.getBoard()[3][2].getTile());
        Assert.assertEquals('.', board.getBoard()[2][2].getTile());
        p.takeAction(Action.MOVE_LEFT, board); // move back
    }

    @Test
    public void testDamageAndDeath() {
        while (p.getHealthCurr() > 0)
            p.takeDamage(100);
        Assert.assertTrue(p.takeDamage(13));
        Assert.assertFalse(board.isPlayerAlive());
    }
}
