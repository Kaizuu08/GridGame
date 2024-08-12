package Fallin.engine;

import java.io.Serializable;

public interface GameItem extends Serializable {
    void interact(Player player);
}
