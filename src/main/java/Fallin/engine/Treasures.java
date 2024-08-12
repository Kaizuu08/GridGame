package Fallin.engine;

import java.io.Serializable;

public class Treasures implements GameItem, Serializable {
    @Override
    public void interact (Player player) {
        player.collectTreasure();
    }
}
