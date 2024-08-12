package Fallin.engine;

import java.io.Serializable;

public class Trap implements GameItem, Serializable {
    @Override
    public void interact(Player player) {
        player.triggerTrap();
    }
}
