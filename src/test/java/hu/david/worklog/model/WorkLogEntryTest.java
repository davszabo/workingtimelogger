package hu.david.worklog.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class WorkLogEntryTest {

    private WorkLogEntry createEntry(double hours, int hourlyWage, boolean outOfCounty) {
        LocalTime start = LocalTime.of(8, 0);
        int minutes = (int) Math.round(hours * 60);
        LocalTime end = start.plusMinutes(minutes);
        return new WorkLogEntry(1, LocalDate.now(), start, end, "Munka", outOfCounty, hourlyWage);
    }

    @Test
    public void testConstructorAndGetters() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(16, 0);
        WorkLogEntry entry = new WorkLogEntry(5, date, start, end, "Leírás", true, 2000);

        assertEquals(5, entry.getId());
        assertEquals(date, entry.getDate());
        assertEquals(start, entry.getStartTime());
        assertEquals(end, entry.getEndTime());
        assertEquals("Leírás", entry.getDescription());
        assertTrue(entry.isOutOfCounty());
        assertEquals(2000, entry.getWage());
        assertEquals(2000, entry.getHourlyWage());
    }

    @Test
    public void testDurationCalculation() {
        WorkLogEntry entry = createEntry(6.5, 2000, false);
        assertEquals(6.5, entry.getDurationHours(), 0.01);
    }

    @Test
    public void testLocationFormatting() {
        WorkLogEntry entry = createEntry(8.0, 2000, false);
        entry.setLocations(Arrays.asList("Budapest", "Debrecen"));
        assertEquals("Budapest, Debrecen", entry.getLocation());
    }

    @Test
    public void testEmptyLocationHandling() {
        WorkLogEntry entry = createEntry(8.0, 2000, false);
        entry.setLocations(Collections.emptyList());
        assertNull(entry.getLocations());
        assertEquals("", entry.getLocation());
    }

    @Test
    public void testToStringFormat() {
        WorkLogEntry entry = createEntry(8.0, 2000, false);
        assertTrue(entry.toString().contains(LocalDate.now().toString()));
        assertTrue(entry.toString().contains("Munka"));
    }

    @Test
    public void testNullSafeLocations() {
        WorkLogEntry entry = createEntry(8.0, 2000, false);
        entry.setLocations(null);
        assertNull(entry.getLocations());
        assertEquals("", entry.getLocation());
    }
}
