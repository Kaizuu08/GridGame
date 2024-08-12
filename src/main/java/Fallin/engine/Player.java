package Fallin.engine;

import java.io.Serializable;

public class Player implements Serializable {
    private int life;
    private int treasuresCollected;
    private int steps;


    public Player() {
        this.life = 10;
        this.treasuresCollected = 0;
        this.steps = 0;
    }
    public void move() {
        this.steps++;
    }

    public void collectTreasure() {
        this.treasuresCollected++;
    }

    public void useMedicalUnit() {
        this.life = Math.min(10, this.life + 3);
    }

    public void triggerTrap() {
        this.life -= 2;
    }

    public void encounterMutant() {
        this.life -= 4;
    }

    public int getLife() {
        return life;
    }

    public int getTreasuresCollected() {
        return treasuresCollected;
    }

    public int getSteps() {
        return steps;
    }

    public void setLife(int i) {
        life = i;
    }

    public double getMaxLives() {
        return 10;
    }

    public double getLives() {
        return this.life;
    }
}
