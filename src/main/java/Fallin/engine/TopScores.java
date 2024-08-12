package Fallin.engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Manages the top score achieved in our game
 */
public class TopScores implements Serializable {
    private static final long serialVersionUID = 1L;
    private static List<ScoreRecord> topScores = new ArrayList<>();

    /**
     * Adds a new score to the list of top scores.
     *
     * @param score the score to add
     */
    public static void addScore(int score) {
        topScores.add(new ScoreRecord(score, new Date()));
        Collections.sort(topScores);
        if (topScores.size() > 5) {
            topScores = topScores.subList(0, 5);
        }
        // Debug statement
        System.out.println("TopScores: " + topScores);
    }

    /**
     * Returns the list of top scores.
     *
     * @return the list of top scores
     */
    public static List<ScoreRecord> getTopScores() {
        return new ArrayList<>(topScores);
    }

    /**
     * Sets the list of top scores.
     *
     * @param scores the list of top scores to set
     */
    public static void setTopScores(List<ScoreRecord> scores) {
        topScores = new ArrayList<>(scores);
    }

    // Custom serialization method
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>(topScores));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        List<ScoreRecord> deserializedTopScores = (List<ScoreRecord>) ois.readObject();
        topScores = new ArrayList<>(deserializedTopScores);
    }

}
