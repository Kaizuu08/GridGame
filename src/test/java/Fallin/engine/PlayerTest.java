import Fallin.engine.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testInitialState() {
        assertEquals(10, player.getLife(), "Initial life should be 10");
        assertEquals(0, player.getTreasuresCollected(), "Initial treasuresCollected should be 0");
        assertEquals(0, player.getSteps(), "Initial steps should be 0");
    }

    @Test
    public void testMove() {
        player.move();
        assertEquals(1, player.getSteps(), "Steps should increment to 1 after move");
    }

    @Test
    public void testCollectTreasure() {
        player.collectTreasure();
        assertEquals(1, player.getTreasuresCollected(), "TreasuresCollected should increment to 1 after collecting treasure");
    }

    @Test
    public void testUseMedicalUnit() {
        player.triggerTrap();
        player.useMedicalUnit();
        assertEquals(10, player.getLife(), "Life should increase by 3 after using medical unit but not exceed 10");
        player.useMedicalUnit();
        assertEquals(10, player.getLife(), "Life should be capped at 10 after using medical unit");
    }

    @Test
    public void testTriggerTrap() {
        player.triggerTrap();
        assertEquals(8, player.getLife(), "Life should decrease by 2 after triggering trap");
    }

    @Test
    public void testEncounterMutant() {
        player.encounterMutant();
        assertEquals(6, player.getLife(), "Life should decrease by 4 after encountering mutant");
    }
}
