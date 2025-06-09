package hu.david.worklog.db;

import hu.david.worklog.model.WorkLogEntry;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:worklog.db";
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    private static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found!", e);
        }
        Connection conn = DriverManager.getConnection(URL);
        // Enable foreign key enforcement so cascading deletes remove related
        // locations when a work_log row is deleted.
        try (Statement s = conn.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void initializeDatabase() {
        String createWorkLogTable = """
            CREATE TABLE IF NOT EXISTS work_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT NOT NULL,
                description TEXT NOT NULL,
                out_of_county BOOLEAN NOT NULL,
                hourly_wage INTEGER NOT NULL
            );
        """;

        String createLocationsTable = """
            CREATE TABLE IF NOT EXISTS locations (
                entry_id INTEGER,
                location TEXT,
                FOREIGN KEY(entry_id) REFERENCES work_log(id) ON DELETE CASCADE
            );
        """; // related rows are removed automatically when the parent entry is deleted

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createWorkLogTable);
            stmt.execute(createLocationsTable);
            LOGGER.log(Level.INFO, "Database created and initialized.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error initializing database", e);
        }
    }

    public static void saveEntry(WorkLogEntry entry) {
        String insertEntry = "INSERT INTO work_log (date, start_time, end_time, description, out_of_county, hourly_wage) VALUES (?, ?, ?, ?, ?, ?)";
        String insertLocation = "INSERT INTO locations (entry_id, location) VALUES (?, ?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(insertEntry, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, entry.getDate().toString());
                pstmt.setString(2, entry.getStartTime().toString());
                pstmt.setString(3, entry.getEndTime().toString());
                pstmt.setString(4, entry.getDescription());
                pstmt.setBoolean(5, entry.isOutOfCounty());
                pstmt.setInt(6, entry.getHourlyWage());
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int entryId = generatedKeys.getInt(1);
                        if (entry.getLocations() != null) {
                            try (PreparedStatement locStmt = conn.prepareStatement(insertLocation)) {
                                for (String location : entry.getLocations()) {
                                    locStmt.setInt(1, entryId);
                                    locStmt.setString(2, location);
                                    locStmt.addBatch();
                                }
                                locStmt.executeBatch();
                            }
                        }
                    }
                }
            }

            conn.commit();
            LOGGER.log(Level.INFO, "Entry saved successfully: " + entry);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving entry", e);
        }
    }

    public static List<WorkLogEntry> loadEntries() {
        List<WorkLogEntry> entries = new ArrayList<>();
        String selectEntries = "SELECT * FROM work_log";
        String selectLocations = "SELECT location FROM locations WHERE entry_id = ?";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectEntries)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                WorkLogEntry entry = new WorkLogEntry(
                        id,
                        LocalDate.parse(rs.getString("date")),
                        LocalTime.parse(rs.getString("start_time")),
                        LocalTime.parse(rs.getString("end_time")),
                        rs.getString("description"),
                        rs.getBoolean("out_of_county"),
                        rs.getInt("hourly_wage")
                );

                try (PreparedStatement locStmt = conn.prepareStatement(selectLocations)) {
                    locStmt.setInt(1, id);
                    try (ResultSet locRs = locStmt.executeQuery()) {
                        List<String> locations = new ArrayList<>();
                        while (locRs.next()) {
                            locations.add(locRs.getString("location"));
                        }
                        entry.setLocations(locations);
                    }
                }

                entries.add(entry);
            }
            LOGGER.log(Level.INFO, "Entries loaded successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading entries", e);
        }
        return entries;
    }

    public static void deleteEntry(int id) {
        String deleteEntry = "DELETE FROM work_log WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteEntry)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Entry deleted, ID: " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting entry", e);
        }
    }
}
