import Fallin.engine.ScoreRecord;
import Fallin.engine.TopScores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopScoresTest {

    @BeforeEach
    public void setUp() {
        // Clear the top scores before each test
        TopScores.setTopScores(List.of());
    }

    @Test
    public void testAddSingleScore() {
        TopScores.addScore(100);
        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(1, scores.size());
        assertEquals(100, scores.get(0).getScore());
    }

    @Test
    public void testAddMultipleScores() {
        TopScores.addScore(200);
        TopScores.addScore(100);
        TopScores.addScore(300);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(3, scores.size());
        assertEquals(300, scores.get(0).getScore());
        assertEquals(200, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }

    @Test
    public void testTopFiveScores() {
        TopScores.addScore(100);
        TopScores.addScore(200);
        TopScores.addScore(300);
        TopScores.addScore(400);
        TopScores.addScore(500);
        TopScores.addScore(600);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(5, scores.size());
        assertEquals(600, scores.get(0).getScore());
        assertEquals(500, scores.get(1).getScore());
        assertEquals(400, scores.get(2).getScore());
        assertEquals(300, scores.get(3).getScore());
        assertEquals(200, scores.get(4).getScore());
    }

    @Test
    public void testSetTopScores() {
        List<ScoreRecord> initialScores = List.of(
                new ScoreRecord(300, new java.util.Date()),
                new ScoreRecord(200, new java.util.Date()),
                new ScoreRecord(100, new java.util.Date())
        );
        TopScores.setTopScores(initialScores);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(3, scores.size());
        assertEquals(300, scores.get(0).getScore());
        assertEquals(200, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }

    @Test
    public void testSerialization() throws Exception {
        TopScores.addScore(100);
        TopScores.addScore(200);

        // Serialize TopScores
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(TopScores.getTopScores());
        oos.close();

        // Deserialize TopScores
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<ScoreRecord> deserializedTopScores = (List<ScoreRecord>) ois.readObject();
        ois.close();

        // Restore deserializedTopScores
        TopScores.setTopScores(deserializedTopScores);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(2, scores.size());
        assertEquals(200, scores.get(0).getScore());
        assertEquals(100, scores.get(1).getScore());
    }

    @Test
    public void testAddingDuplicateScores() {
        TopScores.addScore(200);
        TopScores.addScore(200);
        TopScores.addScore(200);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(3, scores.size());
        for (ScoreRecord score : scores) {
            assertEquals(200, score.getScore());
        }
    }

    @Test
    public void testAddingScoresInDescendingOrder() {
        TopScores.addScore(300);
        TopScores.addScore(200);
        TopScores.addScore(100);

        List<ScoreRecord> scores = TopScores.getTopScores();
        assertEquals(3, scores.size());
        assertEquals(300, scores.get(0).getScore());
        assertEquals(200, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }
}
