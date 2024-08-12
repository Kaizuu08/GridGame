import Fallin.engine.ScoreRecord;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreRecordTest {

    @Test
    void testConstructorAndGetters() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("01/01/2020");
        ScoreRecord record = new ScoreRecord(100, date);

        assertEquals(100, record.getScore());
        assertEquals(date, record.getDate());
    }

    @Test
    void testCompareTo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse("01/01/2020");
        Date date2 = dateFormat.parse("02/02/2021");

        ScoreRecord record1 = new ScoreRecord(100, date1);
        ScoreRecord record2 = new ScoreRecord(200, date2);
        ScoreRecord record3 = new ScoreRecord(50, date1);

        assertTrue(record1.compareTo(record2) > 0);
        assertTrue(record1.compareTo(record3) < 0);
        assertEquals(0, record1.compareTo(new ScoreRecord(100, date2)));
    }

    @Test
    void testToString() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("01/01/2020");
        ScoreRecord record = new ScoreRecord(100, date);

        assertEquals("100 01/01/2020", record.toString());
    }
}
