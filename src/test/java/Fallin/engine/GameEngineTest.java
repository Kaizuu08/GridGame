import Fallin.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(10, 5);
    }

    @Test
    void testInitialization() {
        assertNotNull(engine);
        assertEquals(10, engine.getSize());
        assertEquals(10, engine.getPlayer().getLife()); // Initial life should be 10
        assertEquals(0, engine.getPlayer().getTreasuresCollected());
        assertEquals(0, engine.getPlayer().getSteps());
        assertFalse(engine.isGameEnded());
    }

    @Test
    void testMovePlayer() {
        engine.movePlayer("up");
        assertEquals(8, engine.getPlayerX());
        assertEquals(0, engine.getPlayerY());

        engine.movePlayer("right");
        assertEquals(8, engine.getPlayerX());
        assertEquals(1, engine.getPlayerY());

        engine.movePlayer("down");
        assertEquals(9, engine.getPlayerX());
        assertEquals(1, engine.getPlayerY());

        engine.movePlayer("left");
        assertEquals(9, engine.getPlayerX());
        assertEquals(0, engine.getPlayerY());
    }

    @Test
    void testMovePlayerOutOfBounds() {
        engine.movePlayer("down"); // Initial position is (9,0), move down should be out of bounds
        assertEquals(9, engine.getPlayerX());
        assertEquals(0, engine.getPlayerY());
    }

    @Test
    void testPlaceGameItems() {
        Random rand = new Random();
        int initialItemCount = countItems();
        engine.placeGameItems();
        int newItemCount = countItems();

        assertTrue(newItemCount > initialItemCount);
    }

    private int countItems() {
        int count = 0;
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                if (engine.getMap()[i][j].getItem() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Test
    void testGameStatePersistence() {
        GameEngine.GameState savedState = engine.getGameState();
        engine.movePlayer("up");
        engine.movePlayer("right");

        GameEngine loadedEngine = new GameEngine(savedState);
        assertEquals(savedState.playerX, loadedEngine.getPlayerX());
        assertEquals(savedState.playerY, loadedEngine.getPlayerY());
        assertEquals(savedState.score, loadedEngine.getScore());
        assertEquals(savedState.gameEnded, loadedEngine.isGameEnded());
    }

    @Test
    void testEndGame() {
        engine.endGame(true);
        assertTrue(engine.isGameEnded());
        assertEquals(0, engine.getScore());
    }

    @Test
    void testScoreUpdate() {
        engine.getPlayer().collectTreasure();
        engine.updateScore();
        assertEquals(120, engine.getScore());
    }
}
