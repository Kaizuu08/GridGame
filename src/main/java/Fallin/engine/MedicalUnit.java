package Fallin.engine;

import java.io.Serializable;

public class MedicalUnit implements GameItem, Serializable {
    @Override
    public void interact(Player player) {
        player.useMedicalUnit();
    }
}
