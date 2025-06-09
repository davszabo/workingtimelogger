package hu.david.worklog.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WorkLogEntryTest {

    @Test
    public void testBasicFields() {
        WorkLogEntry entry = new WorkLogEntry(
                1,
                LocalDate.of(2025, 6, 9),
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                "Teszt munka",
                false,
                1500
        );

        assertEquals(1, entry.getId());
        assertEquals("Teszt munka", entry.getDescription());
        assertEquals(1500, entry.getHourlyWage());
        assertEquals(LocalDate.of(2025, 6, 9), entry.getDate());
        assertEquals(LocalTime.of(8, 0), entry.getStartTime());
        assertEquals(LocalTime.of(16, 0), entry.getEndTime());
        assertFalse(entry.isOutOfCounty());
        assertTrue(entry.getLocations().isEmpty());
    }

    @Test
    public void testDurationCalculation() {
        WorkLogEntry entry = new WorkLogEntry(
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 30),
                "Leírás",
                false,
                2000
        );
        double duration = entry.getDurationHours();
        assertEquals(8.5, duration, 0.01);
    }

    @Test
    public void testLocations_SetterAndGetter() {
        WorkLogEntry entry = new WorkLogEntry(
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                "Valami",
                false,
                1500
        );

        assertTrue(entry.getLocations().isEmpty());

        entry.setLocations(Arrays.asList("Budapest", "Debrecen"));
        assertEquals(2, entry.getLocations().size());
        assertTrue(entry.getLocation().contains("Budapest"));
        assertTrue(entry.getLocation().contains("Debrecen"));
    }
}
