package Fallin.engine;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A record of players score and date achieved
 */
public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    private int score;  // score achieved by player
    private Date date; // the date score was achieved

    /**
     * Constructs a ScoreRecord with the given score and date.
     *
     * @param score the score achieved by the player
     * @param date the date when the score was achieved
     */
    public ScoreRecord(int score, Date date) {
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Compares this ScoreRecord with another ScoreRecord based on the score.
     *
     * @param other the other ScoreRecord to compare to
     * @return a negative integer, zero, or a positive integer as this ScoreRecord
     *         is less than, equal to, or greater than the specified ScoreRecord
     */
    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score);
    }

    /**
     * Returns a string representation of the ScoreRecord.
     *
     * @return a string representation of the ScoreRecord
     */
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("%d %s", score, dateFormat.format(date));
    }
}
