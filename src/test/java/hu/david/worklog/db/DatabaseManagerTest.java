package hu.david.worklog.db;

import hu.david.worklog.model.WorkLogEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @BeforeEach
    void setup() {
        new File("worklog.db").delete();
        DatabaseManager.initializeDatabase();
    }

    @AfterEach
    void cleanup() {
        new File("worklog.db").delete();
    }

    @Test
    void testSaveAndLoadEntryWithLocations() {
        WorkLogEntry entry = new WorkLogEntry(
                LocalDate.of(2025, 6, 10),
                LocalTime.of(9, 0),
                LocalTime.of(17, 30),
                "Teszt munka",
                true,
                2000
        );
        entry.setLocations(List.of("Budapest", "Gyor"));

        DatabaseManager.saveEntry(entry);
        List<WorkLogEntry> entries = DatabaseManager.loadEntries();

        assertEquals(1, entries.size());

        WorkLogEntry loaded = entries.get(0);
        assertEquals(entry.getDate(), loaded.getDate());
        assertEquals(entry.getStartTime(), loaded.getStartTime());
        assertEquals(entry.getEndTime(), loaded.getEndTime());
        assertEquals(entry.getDescription(), loaded.getDescription());
        assertEquals(entry.isOutOfCounty(), loaded.isOutOfCounty());
        assertEquals(entry.getHourlyWage(), loaded.getHourlyWage());
        assertNotNull(loaded.getLocations());
        assertEquals(2, loaded.getLocations().size());
        assertTrue(loaded.getLocations().contains("Budapest"));
        assertTrue(loaded.getLocations().contains("Gyor"));
    }

    @Test
    void testDeleteEntryAlsoRemovesLocations() {
        WorkLogEntry entry = new WorkLogEntry(
                LocalDate.of(2025, 6, 11),
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                "Törlés teszt",
                false,
                1800
        );
        entry.setLocations(List.of("Szeged"));

        DatabaseManager.saveEntry(entry);
        List<WorkLogEntry> beforeDelete = DatabaseManager.loadEntries();
        assertEquals(1, beforeDelete.size());

        int entryId = beforeDelete.get(0).getId();
        DatabaseManager.deleteEntry(entryId);

        List<WorkLogEntry> afterDelete = DatabaseManager.loadEntries();
        assertTrue(afterDelete.isEmpty());

        // verify that related location rows are also removed
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:worklog.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM locations")) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
        } catch (SQLException e) {
            fail("Database query failed" + e.getMessage());
        }
    }
}
