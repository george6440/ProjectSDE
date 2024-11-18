package SortingTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ScoresTest {
    private Scores scores;

    @BeforeEach
    public void setUp() {
        scores = new Scores("Player1", LocalDateTime.of(2023, 5, 1, 12, 0),
                LocalDateTime.of(2023, 5, 1, 13, 0), 100, true);
    }

    @Test
    public void testGetters() {
        assertEquals("Player1", scores.getName());
        assertEquals(LocalDateTime.of(2023, 5, 1, 12, 0), scores.getBegin());
        assertEquals(LocalDateTime.of(2023, 5, 1, 13, 0), scores.getEnd());
        assertEquals(100, scores.getSteps());
        assertTrue(scores.isDone());
    }

    @Test
    public void testSetdText() {
        scores.setdText("Finished");
        assertEquals("Finished", scores.getdText());
    }

    @Test
    public void testToString() {
        String expected = "Scores{name='Player1', begin=2023-05-01T12:00, end=2023-05-01T13:00, steps=100, done=true}";
        assertEquals(expected, scores.toString());
    }
}