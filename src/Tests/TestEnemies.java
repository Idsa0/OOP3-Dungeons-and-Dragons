package Tests;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Position;
import Backend.Tiles.Units.Enemies.*;
import org.junit.*;

public class TestEnemies {
    char[][] level =
            {
                    {'#', '#', '#', '#', '#', '#'},
                    {'#', 'z', '.', '.', '.', '#'},
                    {'#', 's', '.', '.', '.', '#'},
                    {'#', '@', '.', '.', '.', '#'},
                    {'#', '.', '.', '.', '.', '#'},
                    {'#', '#', '#', '#', '#', '#'}
            };
    GameBoard board = createBoardWithPath(level);
    Enemy enemyZ = board.getEnemies().get(0);
    Enemy enemyS = board.getEnemies().get(1);

    private static GameBoard createBoardWithPath(char[][] level) {
        GameBoard board = new GameBoard();
        board.createPlayer(3);
        board.setBoard(level);
        board.createPlayer(3);
        return board;
    }

    @Before
    public void setTests() {
    }

    @Test
    public void testMove() {
        Position pos = enemyS.getPosition();
        enemyS.moveLeft(board); // move into a wall
        Assert.assertEquals(pos, enemyS.getPosition());
        Assert.assertEquals('#', board.getBoard()[2][0].getTile());
        Assert.assertEquals('s', board.getBoard()[2][1].getTile());
        enemyS.moveRight(board); // move into a floor
        Assert.assertEquals(2, enemyS.getPosition().getX());
        Assert.assertEquals(2, enemyS.getPosition().getY());
        Assert.assertEquals('s', board.getBoard()[2][2].getTile());
        Assert.assertEquals('.', board.getBoard()[2][1].getTile());
        enemyS.moveLeft(board); // go back;
        int h = enemyZ.getHealthCurr();
        enemyS.moveUp(board); // try to attack another enemy
        Assert.assertEquals(2, enemyS.getPosition().getX());
        Assert.assertEquals(1, enemyS.getPosition().getY());
        Assert.assertEquals('z', board.getBoard()[1][1].getTile());
        Assert.assertEquals('s', board.getBoard()[2][1].getTile());
        Assert.assertEquals(enemyZ.getHealthCurr(), h);
    }

    @Test
    public void testDamageAndDeath() {
        int count = 0;
        while (enemyS.getHealthCurr() > 0) {
            enemyS.takeDamage(20);
            count++;
        }
        Assert.assertEquals(4, count);
        Assert.assertEquals('.', board.getBoard()[2][1].getTile());
        Assert.assertFalse(board.getEnemies().contains(enemyS));
    }
}
