package me.extain.server.objects.Player;

public class PlayerStats {

    private float xp;
    private float xpNeeded;
    private int level, lastLevel;
    private int attack;

    public PlayerStats(float xp, int level, int attack) {
        this.xp = xp;
        this.level = level;
        this.attack = attack;
        this.lastLevel = level;
        this.xpNeeded = 20; // Set low for testing purposes.
    }

    public PlayerStats() {
        this(0, 1, 1);

        this.xpNeeded = 20;
    }

    public void calculateLevel() {
        if (this.xp >= this.xpNeeded) {
            lastLevel = level;

            level +=1;

            xp = xp - xpNeeded;

            xpNeeded = level * 10;
        }
    }


    public float getXp() {
        return xp;
    }

    public int getAttack() {
        return attack;
    }

    public int getLevel() {
        return level;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setXp(float xp) {
        this.xp = xp;
    }

    public void addXp(float xp) {
        this.xp += xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public float getXpNeeded() {
        return xpNeeded;
    }
}
