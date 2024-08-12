package Fallin.engine;

import java.io.Serializable;

public class Mutant implements GameItem, Serializable {
    @Override
    public void interact(Player player) {
        player.encounterMutant();
    }
}
