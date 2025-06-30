package hu.david.worklog.db;

import hu.david.worklog.model.WorkLogEntry;

import java.sql.*;
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
                duration TEXT,
                location TEXT,
                description TEXT NOT NULL,
                calculated_wage TEXT
            );
        """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createWorkLogTable);
            LOGGER.log(Level.INFO, "Database created and initialized.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error initializing database", e);
        }
    }

    public static void saveEntry(WorkLogEntry entry) {
        String insertEntry = "INSERT INTO work_log (date, start_time, end_time, duration, location, description, calculated_wage) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement(insertEntry)) {
                pstmt.setString(1, entry.getDate());
                pstmt.setString(2, entry.getStartTime());
                pstmt.setString(3, entry.getEndTime());
                pstmt.setString(4, entry.getDuration());
                pstmt.setString(5, entry.getLocation()); // most már String!
                pstmt.setString(6, entry.getDescription());
                pstmt.setString(7, entry.getCalculatedWage());
                pstmt.executeUpdate();
            }
            LOGGER.log(Level.INFO, "Entry saved successfully: " + entry);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving entry", e);
        }
    }

    public static List<WorkLogEntry> loadEntries() {
        List<WorkLogEntry> entries = new ArrayList<>();
        String selectEntries = "SELECT * FROM work_log";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectEntries)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("date");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String duration = rs.getString("duration");
                String location = rs.getString("location");
                String description = rs.getString("description");
                String calculatedWage = rs.getString("calculated_wage");

                WorkLogEntry entry = new WorkLogEntry(id, date, startTime, endTime, duration, location, description, calculatedWage);
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
