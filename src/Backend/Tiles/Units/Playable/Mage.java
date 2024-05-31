package Backend.Tiles.Units.Playable;

import Backend.Tiles.GameBoard;
import Backend.Tiles.Units.Enemies.Enemy;

import java.util.LinkedList;
import java.util.Random;

public class Mage extends Player {
    private final int MANA_COST;
    private final int ABILITY_RANGE;

    private int spellPower;
    private int manaCurr;
    private int manaPool;
    private int hitsCount;

    public Mage(String name, int healthCapacity, int attack, int defense,
                int spellPower, int manaPool, int manaCost, int hitsCount, int abilityRange) {
        super(name, healthCapacity, attack, defense);
        this.spellPower = spellPower;
        this.manaPool = manaPool;
        this.manaCurr = manaPool / 4;
        this.MANA_COST = manaCost;
        this.hitsCount = hitsCount;
        this.ABILITY_RANGE = abilityRange;
    }

    @Override
    public String description() {
        return String.format("Mage: %s,\tSpell power: %d,\tMana: %d/%d", super.description(), spellPower, manaCurr, manaPool);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        manaPool += 25 * level;
        manaCurr = Math.min(manaCurr + manaPool / 4, manaPool);
        spellPower += 10 * level;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        // Blizzard
        send(name + " cast Blizzard!");
        if (manaCurr < MANA_COST) {
            send("Casting failed! Not enough mana!");
            return false;
        }
        manaCurr -= MANA_COST;
        int hits = 0;
        LinkedList<Enemy> l = new LinkedList<>();
        board.getEnemies().stream().filter(enemy -> range(enemy) < ABILITY_RANGE).forEachOrdered(l::addFirst);
        while (hits < hitsCount && l.size() > 0) {
            Enemy enemy = l.get(new Random().nextInt(l.size()));
            if (fixedAttack(this, enemy, spellPower))
                l.remove(enemy);
            hits++;
        }
        return true;
    }

    @Override
    public void tick(GameBoard board) {
        manaCurr = Math.min(manaPool, manaCurr + level);
        super.tick(board);
    }

    public int getManaPool() {
        return this.manaPool;
    }

    public int getSpellPower() {
        return this.spellPower;
    }
}
